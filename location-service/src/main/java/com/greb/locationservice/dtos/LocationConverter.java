package com.greb.locationservice.dtos;

import com.greb.locationservice.models.Location;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocationConverter {
    private final ModelMapper modelMapper;

    public Location fromDto(String key, LocationDto dto){
        var location= modelMapper.map(dto, Location.class);
        location.setDriverId(key);
        GeoJsonPoint position= new GeoJsonPoint(
                dto.getPosition().getLongitude(),
                dto.getPosition().getLatitude()
        );
        location.setPosition(position);
        return location;
    }

    public LocationDto toDto(Location location){
        var dto= modelMapper.map(location, LocationDto.class);
        var positionDto= new PositionDto(
                location.getPosition().getX(),
                location.getPosition().getY()
        );
        dto.setPosition(positionDto);
        return dto;
    }
}
