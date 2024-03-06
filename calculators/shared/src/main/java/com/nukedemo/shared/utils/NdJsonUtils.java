package com.nukedemo.shared.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nukedemo.shared.exception.NdException;

import java.io.File;
import java.io.IOException;

public class NdJsonUtils {

    public final static ObjectMapper MAPPER = new ObjectMapper();

    public static String toJson(Object object) throws NdException {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (final IOException e) {
            throw new NdException("JSON serialization failed.", e);
        }
    }

    public static String toPrettyJson(Object object) throws NdException {
        try {
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (final IOException e) {
            throw new NdException("JSON serialization failed.", e);
        }
    }

    public static String toPrettyJson(String str) throws NdException {
        try {
            Object json = MAPPER.readValue(str, Object.class);
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(json);
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

    public static <T> T fromJson(File file, Class<T> clazz) throws NdException {
        try {
            return MAPPER.readValue(file, clazz);
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

    public static <T> T fromJson(File file, TypeReference<T> valueTypeRef) throws NdException {
        try {
            return MAPPER.readValue(file, valueTypeRef);
        } catch (final IOException e) {
            throw new NdException("JSON deserialization failed.", e);
        }
    }

}
