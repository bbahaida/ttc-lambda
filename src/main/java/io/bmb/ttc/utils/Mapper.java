package io.bmb.ttc.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.bmb.ttc.model.TriangleType;
import lombok.SneakyThrows;

public class Mapper {
    private static final ObjectMapper mapper = new ObjectMapper();


    @SneakyThrows
    public static TriangleType convert(String body) {
        return mapper.readValue(body, TriangleType.class);
    }

    @SneakyThrows
    public static String asString(Object body) {
        return mapper.writeValueAsString(body);
    }
}
