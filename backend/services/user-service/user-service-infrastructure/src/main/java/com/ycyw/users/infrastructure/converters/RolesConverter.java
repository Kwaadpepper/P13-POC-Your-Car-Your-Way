package com.ycyw.users.infrastructure.converters;

import java.util.Collections;
import java.util.Set;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Converts a Set of Strings (roles) to a JSON string for database storage and vice versa. This
 * converter is used by JPA to handle the conversion between the entity attribute and the database
 * column.
 */
@Converter
public class RolesConverter implements AttributeConverter<Set<String>, String> {

  private static final ObjectMapper MAPPER = new ObjectMapper();
  private static final TypeReference<Set<String>> TYPE_REF = new TypeReference<>() {};

  @Override
  public String convertToDatabaseColumn(Set<String> attribute) {
    try {
      if (attribute == null || attribute.isEmpty()) {
        return "[]";
      }
      return MAPPER.writeValueAsString(attribute);
    } catch (Exception e) {
      throw new IllegalStateException("Unable to convert roles to JSON", e);
    }
  }

  @Override
  public Set<String> convertToEntityAttribute(String dbData) {
    try {
      if (dbData == null || dbData.isBlank()) {
        return Collections.emptySet();
      }
      return MAPPER.readValue(dbData, TYPE_REF);
    } catch (Exception e) {
      throw new IllegalStateException("Unable to read roles JSON", e);
    }
  }
}
