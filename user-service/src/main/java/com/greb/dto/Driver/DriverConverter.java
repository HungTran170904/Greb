package com.greb.dto.Driver;

import com.greb.dto.User.UserConverter;
import com.greb.model.Driver;
import lombok.AllArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DriverConverter {
    private ModelMapper modelMapper;
    private UserConverter userConverter;

    public Driver fromRegisterDto(RegisterDriverDto dto){
        return modelMapper.map(dto, Driver.class);
    }

    public ResponseDriverDto toResponseDto(Driver driver, UserRepresentation user){
        var driverDto=modelMapper.map(driver, ResponseDriverDto.class);
        if(user!=null) {
            var userDto= userConverter.toDto(user);
            driverDto.setUser(userDto);
        }
        return driverDto;
    }
}
