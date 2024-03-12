package com.example.demo.repository.entity.type;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class JoinStatusConverter implements AttributeConverter<JoinStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(JoinStatus joinStatus) {
        if (joinStatus == null) {
            return null;
        }
        return joinStatus.rawValue();
    }

    @Override
    public JoinStatus convertToEntityAttribute(Integer value) {
        if (value == null) {
            return null;
        }
        return JoinStatus.valueOf(value);
    }
}
