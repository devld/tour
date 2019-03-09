package me.devld.tour.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

public class JsonUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JsonUtil() {
    }

    public static void writeObject(Object o, Writer writer) throws IOException {
        OBJECT_MAPPER.writeValue(writer, o);
    }

    public static void writeObject(Object o, OutputStream os) throws IOException {
        OBJECT_MAPPER.writeValue(os, o);
    }

    public static String objectToString(Object o) {
        try {
            return OBJECT_MAPPER.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
