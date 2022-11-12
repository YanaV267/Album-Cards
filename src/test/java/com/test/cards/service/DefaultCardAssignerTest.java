package com.test.cards.service;

import com.test.cards.domain.Event;
import org.junit.Assert;
import org.junit.Test;

import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.LongStream;

public class DefaultCardAssignerTest {

    private final CardAssigner cardAssigner = new DefaultCardAssigner();
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    private static final long USER_ID = 1;

    @Test
    public void testAssign() {
        Stack<Event> eventQueue = new Stack<>();
        LongStream cardsStream = LongStream.of(1, 3, 2, 2, 4);
        cardAssigner.subscribe(eventQueue::add);
        cardsStream.forEach(card -> cardAssigner.assignCard(USER_ID, card));
        Assert.assertSame(eventQueue.pop().getType(), Event.Type.ALBUM_FINISHED);
        Assert.assertSame(eventQueue.pop().getType(), Event.Type.SET_FINISHED);
        Assert.assertSame(eventQueue.pop().getType(), Event.Type.SET_FINISHED);
    }

}