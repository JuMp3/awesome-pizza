package com.example.awesome_pizza.util;

import com.example.awesome_pizza.exceptions.JsonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class JsonUtils {

    private static final Logger LOG = LogManager.getLogger(JsonUtils.class);

    private JsonUtils() {
    }

    private static final ObjectMapper OBJECT_MAPPER = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .build();

    public static String stringify(Object o) {
        try {
            return OBJECT_MAPPER.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            LOG.error(e.getMessage(), e);
            throw new JsonException(e);
        }
    }

    public static <T> T asPojo(String resource, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(resource, clazz);
        } catch (JsonProcessingException e) {
            throw new JsonException(e);
        }
    }

    public static <T> T asPojo(Resource resource, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(resource.getInputStream(), clazz);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    public static <T> T asPojo(Resource resource, TypeReference<T> typeReference) {
        try {
            return OBJECT_MAPPER.readValue(resource.getInputStream(), typeReference);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    public static <T> T asPojo(byte[] resource, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(resource, clazz);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    public static MultiValueMap<String, String> convert(Object obj) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        Map<String, String> maps = OBJECT_MAPPER.convertValue(obj, new TypeReference<>() {
        });
        parameters.setAll(maps);
        return parameters;
    }

    public static <S extends Collection<S>, T> S asCollection(Resource resource, Class<S> iterable, Class<T> param) {
        try {
            TypeFactory t = TypeFactory.defaultInstance();
            return OBJECT_MAPPER.readValue(resource.getInputStream(),
                    t.constructCollectionType(iterable, param));
        } catch (Exception e) {
            throw new JsonException(e);
        }
    }

    public static JsonNode asJsonNode(Resource resource) {
        try {
            return OBJECT_MAPPER.readValue(resource.getFile(), JsonNode.class);
        } catch (Exception e) {
            throw new JsonException(e);
        }
    }

    public static ObjectMapper objectMapper() {
        return OBJECT_MAPPER;
    }
}

