/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.petstore.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 *
 * @author admin
 */
public class JsonUtil {

    public static Optional<Map.Entry<String, JsonNode>> getColumn(String name,
            Stream<Map.Entry<String, JsonNode>> stream) {
        return stream.filter(byColumn(name)).findFirst();
    }

    public static Stream<Map.Entry<String, JsonNode>> streamFrom(final String json) {
        try {
            final JsonNode node = new ObjectMapper().readValue(json, JsonNode.class);
            final Iterable<Map.Entry<String, JsonNode>> iterable = () -> node.fields();
            return StreamSupport.stream(iterable.spliterator(), false);
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public static String stringValue(final Map.Entry<String, JsonNode> entry) {
        return entry.getValue().toString().toUpperCase().replaceAll("\"", "");
    }
    
    public static String stringValue(Optional<Map.Entry<String, JsonNode>> optional) {
        return optional.get().getValue().toString().replaceAll("\"", "");
    }

    private static Predicate<Map.Entry<String, JsonNode>> byColumn(final String column) {
        return entry -> entry.getKey().equalsIgnoreCase(column);
    }
}
