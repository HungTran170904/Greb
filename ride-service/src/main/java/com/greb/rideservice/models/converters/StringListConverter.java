package com.greb.rideservice.models.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.InternalException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class StringListConverter implements AttributeConverter<List<String>, String> {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        try{
            String data="[]";
            if (attribute != null)
                data=objectMapper.writeValueAsString(attribute);
            return data;
        }
        catch(Exception e){
            log.error(e.getMessage());
            throw new InternalException("Unable to convert String list to JSON string");
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        try{
            List<String> list=new ArrayList();
            if (dbData != null)
                list=objectMapper.readValue(dbData, new TypeReference<>() {});
            return list;
        }
        catch(Exception e){
            log.error(e.getMessage());
            throw new InternalException("Unable to convert JSON string to String list");
        }
    }
}
