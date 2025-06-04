package ru.nsu.lebedev.stringfinder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Class for testing StringFinder class.
 */
public class StringFinderTest {
    private StringFinder finder;

    @BeforeEach
    public void setUp() {
        finder = new StringFinder();
        finder.setLoggingEnabled(false);
    }

    @Test
    void write8gbTestFile() {
        try (FileWriter writer = new FileWriter("8GB.txt")) {
            int fourMbSize = 1048576 * 4;
            StringBuilder fourMbString = new StringBuilder("a");
            fourMbString.append("a".repeat(fourMbSize - 1));
            String fourMbBs = fourMbString.toString();
            fourMbString.replace(1048575, 1048578, "bbb");
            writer.write(fourMbString.toString());
            writer.flush();
            for (int i = 0; i < 2046; ++i) {
                writer.write(fourMbBs);
                writer.flush();
            }
            writer.write(fourMbString.toString());
            writer.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        StringFinder finder = new StringFinder();
        finder.find("8GB.txt", "bbb");
        LinkedList<Long> predictedList = new LinkedList<>();
        predictedList.add(1048575L);
        predictedList.add((long) 8 * 1024 * 1024 * 1024 - 1048578 - 2 * 1048576 + 1L);
        try {
            Files.delete(Paths.get("8GB.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(predictedList, finder.getTargetsPositions());
    }

    @Test
    void testFileWithSpecialCharacters() {
        finder.find("special_chars.txt", "🔥");
        LinkedList<Long> predictedList = new LinkedList<>();
        predictedList.add(56L);
        assertEquals(predictedList, finder.getTargetsPositions());
    }

    @Test
    void russianTest() {
        finder.find("russian.txt", "бра");
        LinkedList<Long> predictedList = new LinkedList<>();
        predictedList.add(1L);
        predictedList.add(8L);
        assertEquals(predictedList, finder.getTargetsPositions());
    }

    @Test
    void frenchTest() {
        finder.find("french.txt", "ç");
        LinkedList<Long> predictedList = new LinkedList<>();
        predictedList.add(17L);
        assertEquals(predictedList, finder.getTargetsPositions());
    }

    @Test
    void koreanTest() {
        finder.find("korean.txt", "오");
        LinkedList<Long> predictedList = new LinkedList<>();
        predictedList.add(40L);
        predictedList.add(43L);
        assertEquals(predictedList, finder.getTargetsPositions());
    }

    @Test
    void fullFileTargetTest() {
        finder.find("russian.txt", "абракадабра");
        LinkedList<Long> predictedList = new LinkedList<>();
        predictedList.add((long) 0);
        assertEquals(predictedList, finder.getTargetsPositions());
    }

    @Test
    void emptyTargetTest() {
        finder.find("russian.txt", "");
        assertTrue(finder.getTargetsPositions().isEmpty());
    }

    @Test
    void targetNotExistTest() {
        finder.find("russian.txt", "sadsad");
        assertTrue(finder.getTargetsPositions().isEmpty());
    }

    @Test
    void fileNotFoundTest() {
        finder.find("not_existed.txt", "sadsad");
        assertTrue(finder.getTargetsPositions().isEmpty());
    }
}
