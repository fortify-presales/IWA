package com.microfocus.example.repository.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

import com.microfocus.example.entity.MfaType;

@Converter(autoApply = true)
public class MfaTypeConverter implements AttributeConverter<MfaType, String> {
 
    @Override
    public String convertToDatabaseColumn(MfaType category) {
        if (category == null) {
            return null;
        }
        return category.getName();
    }

    @Override
    public MfaType convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(MfaType.values())
          .filter(c -> c.getName().equals(code))
          .findFirst()
          .orElseThrow(IllegalArgumentException::new);
    }
}
