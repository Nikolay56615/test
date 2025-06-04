package ru.nsu.lebedev.pizzeria;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests for Json class.
 */
public class JsonTest {
    private record TestObjectType(int test) implements JsonSerializable {}

    @Test
    void testSerializeAndDeserializeString() throws IOException {
        TestObjectType obj = new TestObjectType(5);
        String serialized = Json.serialize(obj);
        Assertions.assertEquals("{\"test\":5}", serialized);

        TestObjectType parsedObj = Json.deserialize(serialized, TestObjectType.class);
        Assertions.assertEquals(obj, parsedObj);
    }

    @Test
    void testSerializeAndDeserializeStream() throws IOException {
        TestObjectType obj = new TestObjectType(5);
        try (
            PipedOutputStream pipedOutputStream = new PipedOutputStream();
                PipedInputStream pipedInputStream = new PipedInputStream(pipedOutputStream)
        ) {
            Json.serialize(obj, pipedOutputStream);
            pipedOutputStream.close();

            TestObjectType parsedObj = Json.deserialize(pipedInputStream, TestObjectType.class);
            Assertions.assertEquals(obj, parsedObj);
        }
    }

    @Test
    void testParsingInvalidJsonThrowsException() {
        String invalidJson = "{\"test\":\"aaa\"}";
        Assertions.assertThrows(ParsingException.class, () -> {
            Json.deserialize(invalidJson, TestObjectType.class);
        });
    }
}
