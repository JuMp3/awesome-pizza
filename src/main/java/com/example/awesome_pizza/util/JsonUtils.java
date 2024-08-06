package com.example.awesome_pizza.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;

public class JsonUtils {

    private JsonUtils() {
    }

    private static final ObjectMapper OBJECT_MAPPER = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .build();

    public static String stringify(Object o) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(o);
    }

    public static <T> T asPojo(String resource, Class<T> clazz) throws JsonProcessingException {
        return OBJECT_MAPPER.readValue(resource, clazz);
    }

    public static <T> T asPojo(byte[] resource, Class<T> clazz) throws IOException {
        return OBJECT_MAPPER.readValue(resource, clazz);
    }
}

