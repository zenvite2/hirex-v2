package com.ptit.data.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@Slf4j
@Converter
public class HashMapConverter<K, V> implements AttributeConverter<HashMap<K, V>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(HashMap<K, V> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting HashMap to JSON string", e);
        }
    }

    @Override
    public HashMap<K, V> convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null) {
                throw new IllegalArgumentException("dbData is null");
            }
            return objectMapper.readValue(dbData, HashMap.class);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
