package ru.nsu.lebedev.pizzeria;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Pizzeria class for managing the pizzeria's operations and thread launching.
 */
public class Pizzeria implements Runnable {
    public static final long TIME_STEP_MS = 100;
    public static final long ORDER_TIMEOUT_MS = 1000;

    private final long workDuration;
    private volatile boolean isClosed = false;

    private ArrayList<Baker> bakerList;
    private EmployeesManager<ThreadBaker> bakerManager;
    private ArrayList<Courier> courierList;
    private EmployeesManager<ThreadCourier> courierManager;
    private MyBlockingQueue<Order> storageQueue;
    private MyBlockingQueue<Order> orderQueue;

    private final String configSavePath;

    /**
     * Constructor.
     * <br>
     * Loads data from the specified load path and initializes internal data.
     *
     * @param workDuration time in conventional units
     * @param configLoadPath path to initialize JSON setup
     * @param configSavePath path for final JSON setup
     * @throws IOException if any error occurs while parsing the input JSON setup
     * @throws NullPointerException if the input JSON setup contains invalid data
     */
    public Pizzeria(long workDuration, String configLoadPath,
                    String configSavePath) throws IOException {
        System.out.println("Initializing Pizzeria");
        this.workDuration = workDuration;
        this.configSavePath = configSavePath;
        Setup config = loadConfig(configLoadPath);
        validateConfig(config);
        bakerList = config.bakers();
        courierList = config.couriers();
        storageQueue = new MyBlockingQueue<>(config.warehouseCapacity());
        orderQueue = new MyBlockingQueue<>(config.orders());
        System.out.println("Pizzeria initialized");
    }

    /**
     * Checks if the pizzeria has not finished its work day.
     *
     * @return true if the pizzeria is still running, false otherwise
     */
    public boolean isOpen() {
        return !isClosed;
    }

    /**
     * Loads the setup configuration from the specified path.
     *
     * @param configPath path to the JSON setup file
     * @return the parsed Setup object
     * @throws IOException if an error occurs while loading or parsing the setup
     */
    private Setup loadConfig(String configPath) throws IOException {
        System.out.println("Loading configuration from " + configPath);
        InputStream inputStream;
        Setup config = null;
        try {
            inputStream = new FileInputStream(configPath);
        } catch (IOException e) {
            inputStream = getClass().getClassLoader().getResourceAsStream(configPath);
        }
        if (inputStream == null) {
            throw new FileNotFoundException("Could not find configuration file at " + configPath);
        }
        config = Json.deserialize(inputStream, Setup.class);
        inputStream.close();
        return config;
    }

    /**
     * Saves the current setup to the specified path.
     */
    private void saveConfig() {
        System.out.println("Saving configuration to " + configSavePath);
        Setup config = new Setup(bakerList, courierList,
                storageQueue.maxSize(), orderQueue.getListCopy());
        try (OutputStream outputStream = new FileOutputStream(configSavePath)) {
            Json.serialize(config, outputStream);
        } catch (IOException e) {
            System.out.println("Failed to save configuration: " + e.getMessage());
        }
    }

    /**
     * Validates the setup data.
     *
     * @param config the setup object to validate
     * @throws IllegalArgumentException if the setup data is invalid
     */
    private void validateConfig(Setup config) {
        if (config.bakers().isEmpty()) {
            throw new IllegalArgumentException("No bakers in configuration");
        }
        if (config.couriers().isEmpty()) {
            throw new IllegalArgumentException("No couriers in configuration");
        }
        if (config.warehouseCapacity() <= 0) {
            throw new IllegalArgumentException("Invalid warehouse capacity");
        }
    }

    /**
     * Runs the pizzeria operations.
     */
    @Override
    public void run() {
        System.out.println("Pizzeria is now open");
        startEmployees();
        try {
            Thread.sleep(workDuration * TIME_STEP_MS);
        } catch (InterruptedException e) {
            System.out.println("Pizzeria interrupted before closing time");
        }
        System.out.println("Closing Pizzeria");
        shutDownEmployees();
        System.out.println("Pizzeria is closed");
    }

    /**
     * Starts the employees (bakers and couriers) using EmployeesManager.
     */
    private void startEmployees() {
        ArrayList<ThreadBaker> bakerThreads = new ArrayList<>();
        for (var baker : bakerList) {
            bakerThreads.add(new ThreadBaker(baker, orderQueue, storageQueue));
        }
        bakerManager = new EmployeesManager<>(bakerThreads);
        bakerManager.startEmployees();

        ArrayList<ThreadCourier> courierThreads = new ArrayList<>();
        for (var courier : courierList) {
            courierThreads.add(new ThreadCourier(courier, orderQueue, storageQueue));
        }
        courierManager = new EmployeesManager<>(courierThreads);
        courierManager.startEmployees();
    }

    /**
     * Finishes the work day by shutting down employees and saving the setup.
     */
    private void shutDownEmployees() {
        bakerManager.offerEmployeesFinishJob();
        orderQueue.close();
        try {
            bakerManager.shutdownAndAwaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.out.println("Baker shutdown interrupted");
        }
        storageQueue.close();
        System.out.println("All bakers have finished");

        courierManager.offerEmployeesFinishJob();
        try {
            courierManager.shutdownAndAwaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.out.println("Courier shutdown interrupted");
        }
        System.out.println("All couriers have finished");

        saveConfig();
        isClosed = true;
    }
}