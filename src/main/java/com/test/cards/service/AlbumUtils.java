package com.test.cards.service;

import com.google.common.collect.Sets;
import com.test.cards.domain.Album;
import com.test.cards.domain.AlbumSet;
import com.test.cards.domain.Card;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class AlbumUtils {

    /**
     *
     * @param userCards cards that is assigned to user.
     * @param album album from configuration.
     * @param albumSetToExcludeFromCheck in case of set is full, we can exclude it from checks because it is already
     *                                   filled, so we already know that userCards contains all of set's cards.
     */
    public boolean isAlbumFull(Set<Card> userCards, Album album, AlbumSet albumSetToExcludeFromCheck) {
        List<Card> albumCards = album.getSets().stream()
                .filter(albumSet -> albumSet.getId() != albumSetToExcludeFromCheck.getId())
                .flatMap(albumSet -> albumSet.getCards().stream())
                .collect(Collectors.toList());
        return userCards.containsAll(albumCards);
    }

    public AlbumSet createFirstAlbumSet() {
        return AlbumSet.builder()
                .id(1)
                .name("First Album Set")
                .cards(Sets.newHashSet(createCard(1, "Card1"), createCard(2, "Card2")))
                .build();
    }

    public AlbumSet createSecondAlbumSet() {
        return AlbumSet.builder()
                .id(2)
                .name("Second Album Set")
                .cards(Sets.newHashSet(createCard(3, "Card3"), createCard(4, "Card4")))
                .build();
    }

    private Card createCard(long id, String name) {
        return Card.builder()
                .id(id)
                .name(name)
                .build();
    }
}
