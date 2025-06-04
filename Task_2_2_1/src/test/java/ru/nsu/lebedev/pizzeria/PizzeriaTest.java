package ru.nsu.lebedev.pizzeria;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests for Pizzeria.
 */
public class PizzeriaTest {
    static void removeFile(String path) {
        File file = new File(path);
        if (!file.delete()) {
            Assertions.fail();
        }
    }

    @Test
    void setupNotFoundTest() {
        Assertions.assertThrows(IOException.class, () -> {
            new Thread(
                    new Pizzeria(
                        50, "Doesnt_exist.json",
                        "Nno"
                    )
            ).start();
        });
    }

    @Test
    void parsingExceptionTest() {
        Assertions.assertThrows(ParsingException.class, () -> {
            new Thread(
                    new Pizzeria(
                        50, "inavalid.json",
                        "Nno"
                    )
            ).start();
        });
    }

    @Test
    void noBakersTest() throws IOException {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Thread(
                    new Pizzeria(
                        50, "noBakers.json",
                        "Nno"
                    )
            ).start();
        });
    }

    @Test
    void noCouriersTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Thread(
                    new Pizzeria(
                        50, "noCouriers.json",
                        "Nno"
                    )
            ).start();
        });
    }

    @Test
    void zeroWarehouseCapacityTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Thread(
                    new Pizzeria(
                        50, "zeroWarehouseCapacity.json",
                        "Nno"
                    )
            ).start();
        });
    }

    @Test
    void testSimplePizzeriaOperation() {
        String resultPath = "test.json";
        long workDuration = 50;
        Pizzeria pizzeria;
        try {
            pizzeria = new Pizzeria(workDuration, "simple.json", resultPath);
        } catch (IOException | IllegalArgumentException e) {
            Assertions.fail(e);
            return;
        }
        Thread pizzeriaThread = new Thread(pizzeria);
        pizzeriaThread.start();

        try {
            Thread.sleep(2 * workDuration * Pizzeria.TIME_STEP_MS);
        } catch (InterruptedException e) {
            Assertions.fail(e);
        }
        while (pizzeria.isOpen()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Assertions.fail(e);
            }
        }
        Assertions.assertFalse(pizzeriaThread.isAlive());

        try (InputStream resultFileStream = new FileInputStream(resultPath)) {
            Optional<Setup> setupOpt =
                Optional.of(Json.deserialize(resultFileStream, Setup.class));
            Assertions.assertTrue(setupOpt.orElseThrow().orders().isEmpty());
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail(e);
        }
        removeFile(resultPath);
    }

    @Test
    void testSmallWarehouseScenario() {
        String resultPath = "test.json";
        long workDuration = 50;
        Pizzeria pizzeria;
        try {
            pizzeria = new Pizzeria(workDuration,
                "smallWarehouseCapacity.json", resultPath);
        } catch (IOException | IllegalArgumentException e) {
            Assertions.fail(e);
            return;
        }
        Thread pizzeriaThread = new Thread(pizzeria);
        pizzeriaThread.start();

        try {
            Thread.sleep(2 * workDuration * Pizzeria.TIME_STEP_MS);
        } catch (InterruptedException e) {
            Assertions.fail(e);
        }

        while (pizzeria.isOpen()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Assertions.fail(e);
            }
        }

        Assertions.assertFalse(pizzeriaThread.isAlive());

        try (InputStream resultFileStream = new FileInputStream(resultPath)) {
            Optional<Setup> setupOpt =
                Optional.of(Json.deserialize(resultFileStream, Setup.class));
            Assertions.assertTrue(setupOpt.orElseThrow().orders().isEmpty());
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail(e);
        }
        removeFile(resultPath);
    }

    @Test
    void testMultiDaySimulation() {
        String resultPath = "test.json";
        long workDuration = 6;
        Pizzeria pizzeria;
        try {
            pizzeria = new Pizzeria(workDuration, "simple.json", resultPath);
        } catch (IOException | IllegalArgumentException e) {
            Assertions.fail(e);
            return;
        }
        Thread pizzeriaThread = new Thread(pizzeria);
        pizzeriaThread.start();

        try {
            Thread.sleep(2 * workDuration * Pizzeria.TIME_STEP_MS);
        } catch (InterruptedException e) {
            Assertions.fail(e);
        }

        while (pizzeria.isOpen()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Assertions.fail(e);
            }
        }

        try (InputStream resultFileStream = new FileInputStream(resultPath)) {
            Optional<Setup> setupOpt =
                Optional.of(Json.deserialize(resultFileStream, Setup.class));
            Assertions.assertFalse(setupOpt.orElseThrow().orders().isEmpty());
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail(e);
        }
        Assertions.assertFalse(pizzeriaThread.isAlive());

        String resultPath2 = "test2.json";
        try {
            pizzeria = new Pizzeria(workDuration, resultPath, resultPath2);
        } catch (IOException | IllegalArgumentException e) {
            Assertions.fail(e);
            return;
        }
        pizzeriaThread = new Thread(pizzeria);
        pizzeriaThread.start();

        try {
            Thread.sleep(2 * workDuration * Pizzeria.TIME_STEP_MS);
        } catch (InterruptedException e) {
            Assertions.fail(e);
        }

        while (pizzeria.isOpen()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Assertions.fail(e);
            }
        }

        try (InputStream resultFileStream = new FileInputStream(resultPath2)) {
            Optional<Setup> setupOpt =
                Optional.of(Json.deserialize(resultFileStream, Setup.class));
            Assertions.assertTrue(setupOpt.orElseThrow().orders().isEmpty());
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail(e);
        }
        Assertions.assertFalse(pizzeriaThread.isAlive());
        removeFile(resultPath);
        removeFile(resultPath2);
    }
}
