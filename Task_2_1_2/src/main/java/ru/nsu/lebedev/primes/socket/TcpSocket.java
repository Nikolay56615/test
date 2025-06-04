package ru.nsu.lebedev.primes.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import ru.nsu.lebedev.primes.errors.ErrorParsingJson;
import ru.nsu.lebedev.primes.errors.ErrorSocketStreamEnd;
import ru.nsu.lebedev.primes.json.Json;
import ru.nsu.lebedev.primes.json.JsonSerializable;

/**
 * Utility class for sending and receiving JSON objects over TCP sockets.
 */
public final class TcpSocket {

    /**
     * Private constructor to prevent instantiation, as this is a utility class.
     */
    private TcpSocket() {
    }

    /**
     * Sends an object serialized to JSON through the specified socket.
     *
     * @param socket the socket to send through
     * @param object the object to serialize and send, must implement JsonSerializable
     * @throws IOException if an I/O error occurs while sending or serializing
     */
    public static void postJsonObject(
        Socket socket, JsonSerializable object
    ) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
        writer.write(Json.serialize(object) + "\n");
        writer.flush();
    }

    /**
     * Receives a JSON object from the specified socket.
     * Deserializes it into an object of the given type.
     *
     * @param socket the socket to receive from
     * @param type the class type of the object to deserialize into, must implement JsonSerializable
     * @return the deserialized object
     * @throws ErrorSocketStreamEnd if the end of the stream is reached prematurely
     * @throws ErrorParsingJson     if there is an error parsing the JSON
     * @throws IOException          if an I/O error occurs while receiving
     */
    public static <T extends JsonSerializable> T getJsonObject(
        Socket socket, Class<T> type
    ) throws ErrorSocketStreamEnd, ErrorParsingJson, IOException {
        InputStream inputStream = socket.getInputStream();
        return Json.deserialize(inputStream, type);
    }

    /**
     * Reads a line of text from the specified socket.
     *
     * @param socket the socket to read from
     * @return the line of text read, or null if the end of the stream is reached
     * @throws IOException if an I/O error occurs while reading
     */
    public static String getString(
        Socket socket
    ) throws IOException {
        InputStream inputStream = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
            inputStream, StandardCharsets.UTF_8
        ));
        return reader.readLine();
    }
}
