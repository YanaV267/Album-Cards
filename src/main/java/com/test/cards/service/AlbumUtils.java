package com.test.cards.service;

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
}
