package com.test.cards.service;

import com.google.common.collect.Sets;
import com.test.cards.domain.Album;
import com.test.cards.domain.AlbumSet;
import com.test.cards.domain.Card;

public class ConfigurationProviderImpl implements ConfigurationProvider {
    @Override
    public Album get() {
        return Album.builder()
                .id(1)
                .name("My Album")
                .sets(Sets.newHashSet(createFirstAlbumSet(), createSecondAlbumSet()))
                .build();
    }

    private AlbumSet createFirstAlbumSet() {
        return AlbumSet.builder()
                .id(1)
                .name("First Album Set")
                .cards(Sets.newHashSet(createCard(1, "Card1"), createCard(2, "Card2")))
                .build();
    }

    private AlbumSet createSecondAlbumSet() {
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
