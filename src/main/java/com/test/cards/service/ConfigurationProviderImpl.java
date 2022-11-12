package com.test.cards.service;

import com.google.common.collect.Sets;
import com.test.cards.domain.Album;

public class ConfigurationProviderImpl implements ConfigurationProvider {

    @Override
    public Album get() {
        return Album.builder()
                .id(1)
                .name("My Album")
                .sets(Sets.newHashSet(AlbumUtils.createFirstAlbumSet(), AlbumUtils.createSecondAlbumSet()))
                .build();
    }

}
