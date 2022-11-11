package com.test.cards.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    private long userId;
    private Type type;

    public enum Type {
        SET_FINISHED, ALBUM_FINISHED
    }
}
