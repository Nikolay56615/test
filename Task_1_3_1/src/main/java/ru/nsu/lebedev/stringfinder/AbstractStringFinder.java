package ru.nsu.lebedev.stringfinder;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

/**
 * Abstract class for string search.
 * Provides basic methods for opening a file, reading segments,
 * and searching for a substring.
 */
public abstract class AbstractStringFinder {
    static final int CAPACITY = 1048576; // 1 MB
    static final int FALSE_VALUE = -1;
    private boolean loggingEnabled = true;
    protected StringBuilder buffer = new StringBuilder();
    protected BufferedReader reader = null;
    protected String searchTarget = "";
    protected LinkedList<Long> targetsPositions = new LinkedList<>();

    /**
     * Default constructor.
     */
    public AbstractStringFinder() {
    }

    /**
     * Enables or disables logging.
     *
     * @param enabled true to enable logging; false to disable
     */
    public void setLoggingEnabled(boolean enabled) {
        this.loggingEnabled = enabled;
    }

    /**
     * Logs a message to the console if logging is enabled.
     *
     * @param message the message to log
     */
    private void log(String message) {
        if (loggingEnabled) {
            System.err.println(message);
        }
    }

    /**
     * Opens a file for reading.
     * Tries to open the file from the filesystem first, and if not found,
     * tries to open it from resources.
     *
     * @param filename path to the file
     * @return true if the file was successfully opened false otherwise
     */
    private boolean openFile(String filename) {
        if (tryOpenFile(filename, false)) {
            return true;
        }
        return tryOpenFile(filename, true);
    }

    /**
     * Openning a file either from the filesystem or from the resources folder.
     *
     * @param filename path to the file
     * @param fromResources if true, tries to open the file from the resources folder;
     *                      if false, tries to open it from the filesystem.
     * @return true if the file was successfully opened false otherwise
     */
    private boolean tryOpenFile(String filename, boolean fromResources) {
        try {
            InputStream inputStream;
            if (fromResources) {
                inputStream = getClass().getClassLoader().getResourceAsStream(filename);
                if (inputStream == null) {
                    log("Resource file not found: " + filename);
                    return false;
                }
            } else {
                inputStream = new FileInputStream(filename);
            }
            InputStreamReader inputStreamReader =
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            reader = new BufferedReader(inputStreamReader, CAPACITY);
            return true;
        } catch (FileNotFoundException e) {
            log("File not found: " + filename);
            return false;
        }
    }

    /**
     * Closes the BufferedReader if it was opened.
     */
    private void closeFile() {
        try {
            if (reader != null) {
                reader.close();
                reader = null;
            }
        } catch (IOException e) {
            log("Error while closing the file: " + e.getMessage());
        }
    }

    /**
     * Reads a segment of text from the file.
     * Reads characters from the file up to the CAPACITY value.
     *
     * @return the number of characters read or -1 if end of file
     */
    protected int readSegment() {
        char[] charBuffer = new char[CAPACITY];
        int readCharsCount;
        try {
            readCharsCount = reader.read(charBuffer);
            if (readCharsCount == FALSE_VALUE) {
                return readCharsCount;
            }
        } catch (IOException e) {
            log("Error reading segment: " + e.getMessage());
            return FALSE_VALUE;
        }
        if (buffer.length() > 0) {
            buffer.delete(0, buffer.length());
        }
        buffer.append(charBuffer, 0, readCharsCount);
        return readCharsCount;
    }

    /**
     * Starts searching for a substring in the file.
     * Finds all starting indices of `target` occurrences in the file.
     * If the file was not opened, the result will be empty.
     *
     * @param filename path to the file for searching
     * @param target substring to search for
     */
    public void find(String filename, String target) {
        if (target == null || target.isEmpty()) {
            log("Empty or null target string.");
            return;
        }
        setSearchTarget(target);
        targetsPositions.clear();
        boolean isOpened = openFile(filename);
        if (!isOpened) {
            return;
        }
        findingSubstring();
        closeFile();
    }

    /**
     * Main search method to be implemented in a subclass.
     * This method defines the logic for searching for the `searchTarget` substring.
     */
    protected abstract void findingSubstring();

    /**
     * Returns a list of starting indices of each occurrence of the substring
     * found after the last call to `find()`.
     *
     * @return LinkedList with starting indices of each occurrence of `searchTarget`
     */
    public LinkedList<Long> getTargetsPositions() {
        return targetsPositions;
    }

    /**
     * Sets the substring to search for.
     *
     * @param target substring to search for
     */
    private void setSearchTarget(String target) {
        this.searchTarget = target;
    }
}
