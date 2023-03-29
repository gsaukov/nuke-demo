package com.nukedemo.core.services.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nukedemo.core.services.exceptions.NdException;

import java.io.IOException;

public class NdJsonUtils {

    private final static ObjectMapper MAPPER = new ObjectMapper();

    public static String toJson(Object object) throws NdException {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (final IOException e) {
            throw new NdException("JSON serialization failed.", e);
        }
    }

    public static <T> T fromJson(String json, Class<T> clazz) throws NdException {
        try {
            return MAPPER.readValue(json, clazz);
        } catch (final IOException e) {
            throw new NdException("JSON deserialization failed.", e);
        }
    }

    public static <T> T fromJson(String json, TypeReference<T> valueTypeRef) throws NdException {
        try {
            return MAPPER.readValue(json, valueTypeRef);
        } catch (final IOException e) {
            throw new NdException("JSON deserialization failed.", e);
        }
    }

}
