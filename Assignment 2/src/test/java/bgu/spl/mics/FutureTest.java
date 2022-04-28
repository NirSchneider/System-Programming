package bgu.spl.mics;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterAll;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class FutureTest {

    private Future<String> future;

    @BeforeEach
    void setUp() {future = new Future<>();}

    @Test
    void get() {
        assertFalse(future.isDone());
        future.resolve("");
        future.get();
        assertTrue(future.isDone());
    }

    @Test
    void resolve() {
        String s = "Result_X";
        future.resolve(s);
        assertTrue(future.isDone());
        assertTrue(s.equals((future.get())));
    }

    @Test
    void isDone() {
        String s = "Result_X";
        assertFalse(future.isDone());
        future.resolve(s);
        assertTrue(future.isDone());
    }

    @Test
    void testGet() {
        assertFalse(future.isDone());
        future.get(100, TimeUnit.MILLISECONDS);
        assertFalse(future.isDone());
        future.resolve("foo");
        assertEquals(future.get(100, TimeUnit.MILLISECONDS),"foo");
    }
}