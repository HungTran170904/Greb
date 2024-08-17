package com.greb.fleetmanagementservice.model.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greb.fleetmanagementservice.model.enums.VehicleType;
import jakarta.persistence.AttributeConverter;
import org.apache.logging.log4j.util.InternalException;

import java.util.ArrayList;
import java.util.List;

public class VehicletypeListConverter implements AttributeConverter<List<VehicleType>, String> {
    private final ObjectMapper objectMapper=new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<VehicleType> attribute) {
        try {
            String dbData="[]";
            if(attribute!=null&&attribute.size()>0)
                dbData=objectMapper.writeValueAsString(attribute);
            return dbData;
        } catch (Exception e) {
            throw new InternalException("Unable to convert Integer list to JSON string");
        }
    }

    @Override
    public List<VehicleType> convertToEntityAttribute(String dbData) {
        try {
            List<VehicleType> list= new ArrayList();
            if(dbData!=null) {
                list=objectMapper.readValue(dbData,new TypeReference<List<VehicleType>>(){});
            }
            return list;
        } catch (Exception e) {
            throw new InternalException("Unable to convert JSON string to VehicleType list");
        }
    }
}
