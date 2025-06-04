package ru.nsu.lebedev.primes.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import ru.nsu.lebedev.primes.errors.ErrorParsingJson;
import ru.nsu.lebedev.primes.errors.ErrorSocketStreamEnd;

/**
 * Utility class for JSON serialization and parsing using Jackson. This class follows the
 * <strong>Single Responsibility Principle</strong> by focusing solely on JSON conversion.
 * Moreover,
 * by relying on the abstract marker interface {@code JsonSerializable}, it observes the
 * <strong>Dependency Inversion Principle</strong>.
 * Note: The parameter type {@code JsonSerializable} is
 * a marker interface used to indicate that an object for JSON.
 */
public final class Json {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Private constructor to prevent instantiation, as this is a utility class.
     */
    private Json() {
    }

    /**
     * Serializes an object to its JSON string representation.
     *
     * @param object an instance of a class implementing {@code JsonSerializable}.
     * @return the JSON string representation of the object.
     * @throws IOException if an error occurs during serialization.
     */
    public static String serialize(JsonSerializable object) throws IOException {
        return MAPPER.writeValueAsString(object);
    }

    /**
     * Deserializes a JSON string into an object of the specified type.
     *
     * @param inputString the JSON string to be deserialized
     * @param type        the target class type
     * @param <T>         the type of the returned object
     * @return an instance of type T deserialized from the JSON string
     * @throws ErrorParsingJson if an error occurs during parsing
     */
    public static <T extends JsonSerializable> T deserialize(String inputString, Class<T> type)
        throws ErrorParsingJson {
        try {
            return MAPPER.readValue(inputString, type);
        } catch (IOException e) {
            throw new ErrorParsingJson(e);
        }
    }

    /**
     * Deserializes JSON data from an input stream into an object of the specified type.
     * The method reads the input stream using UTF-8 and converts it into the target object.
     *
     * @param inputStream the input stream containing JSON data
     * @param type        the target class type
     * @param <T>         the type of the returned object
     * @return an instance of type T deserialized from the JSON input stream
     * @throws ErrorParsingJson if an error occurs during parsing
     * @throws ErrorSocketStreamEnd if an error occurs for closed stream
     */
    public static <T extends JsonSerializable> T deserialize(InputStream inputStream,
        Class<T> type) throws ErrorParsingJson, ErrorSocketStreamEnd {
        BufferedReader reader = new
            BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        try {
            var deserializer = MAPPER.createParser(reader);
            return deserializer.readValueAs(type);
        } catch (MismatchedInputException endOfFileException) {
            throw new ErrorSocketStreamEnd(endOfFileException);
        } catch (IOException e) {
            throw new ErrorParsingJson(e);
        }
    }
}

