package com.test.cards.service;

import com.test.cards.domain.Event;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.LongStream;

public class DefaultCardAssignerTest {

    private CardAssigner cardAssigner = new DefaultCardAssigner();
    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    private static final long USER_ID = 1;

    @Test
    public void testAssign() {
        Queue<Event> eventQueue = new LinkedList<>();
        LongStream cardsStream = LongStream.of(1, 3, 2, 2, 4);
        cardAssigner.subscribe(eventQueue::add);
        cardsStream.forEach(card -> cardAssigner.assignCard(USER_ID, card));
        Assert.assertTrue(eventQueue.poll().getType().equals(Event.Type.ALBUM_FINISHED));
        Assert.assertTrue(eventQueue.poll().getType().equals(Event.Type.SET_FINISHED));
        Assert.assertTrue(eventQueue.poll().getType().equals(Event.Type.SET_FINISHED));
    }

}