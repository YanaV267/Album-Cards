package com.test.cards.service;

import com.test.cards.domain.Album;
import com.test.cards.domain.AlbumSet;
import com.test.cards.domain.Card;
import com.test.cards.domain.Event;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DefaultCardAssigner implements CardAssigner {

    /**
     * ConcurrentHashMap used to support concurrent environment while using USER_TO_CARDS map.
     */
    private static final Map<Long, Set<Card>> USER_TO_CARDS = new ConcurrentHashMap<>();
    private static final Album ALBUM = new ConfigurationProviderImpl().get();
    private static final List<Long> FINISHED_SETS_ID = new ArrayList<>();
    private final Object lock = new Object();

    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    private final List<Consumer<Event>> consumers = new ArrayList<>();


    /**
     * Create sets of cards for future checks to not recreate it every time.
     */
    private static final Map<Long, Card> CARD_ID_TO_ALBUM_CARD = ALBUM.getSets().stream()
            .flatMap(albumSet -> albumSet.getCards().stream())
            .collect(Collectors.toMap(Card::getId, Function.identity()));


    @Override
    public void assignCard(long userId, long cardId) {
        //HashSet is used instead of its concurrent analogs because of contains and add operation is inside
        // synchronised block, so we only care about sequence of "contains" and "add" methods calls. After that we're
        // creating unmodifiable collection to be sure that we can perform needed set or album fill checks and
        // publish events based on state that is actual for the time we're assigning card to user.
        Set<Card> userCards = USER_TO_CARDS.computeIfAbsent(userId, userIdFromMap -> new HashSet<>());
        Card card = CARD_ID_TO_ALBUM_CARD.get(cardId);
        Set<Card> stateOfUserCards;
        synchronized (lock) {
            if (userCards.contains(card)) {
                return;
            }
            userCards.add(card);
            stateOfUserCards = Collections.unmodifiableSet(userCards);
            publishEvents(userId, stateOfUserCards);
        }
    }

    private void publishEvents(long userId, Set<Card> userCards) {
        ALBUM.getSets().forEach(albumSet -> {
            boolean albumSetIsFull = userCards.containsAll(albumSet.getCards());
            if (albumSetIsFull && !FINISHED_SETS_ID.contains(albumSet.getId())) {
                processSetIsFull(userId, userCards, albumSet);
            }
        });
    }

    private void processSetIsFull(long userId, Set<Card> userCards, AlbumSet albumSet) {
        publishSetIsFullEvent(userId);
        FINISHED_SETS_ID.add(albumSet.getId());
        checkFullAlbum(userId, userCards, albumSet);
    }

    private void checkFullAlbum(long userId, Set<Card> userCards, AlbumSet albumSet) {
        boolean isAlbumIsFull = AlbumUtils.isAlbumFull(userCards, ALBUM, albumSet);
        if (isAlbumIsFull) {
            publishAlbumIsFullEvent(userId);
        }
    }

    private void publishAlbumIsFullEvent(long userId) {
        consumers.forEach(consumer -> consumer.accept(Event.builder()
                .type(Event.Type.ALBUM_FINISHED)
                .userId(userId)
                .build()));
    }

    private void publishSetIsFullEvent(long userId) {
        consumers.forEach(consumer -> consumer.accept(Event.builder()
                .type(Event.Type.SET_FINISHED)
                .userId(userId)
                .build()));
    }

    @Override
    public void subscribe(Consumer<Event> consumer) {
        consumers.add(consumer);
    }
}
