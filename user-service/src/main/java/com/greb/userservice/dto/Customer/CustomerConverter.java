package com.greb.userservice.dto.Customer;

import com.greb.userservice.dto.User.UserConverter;
import com.greb.userservice.model.Customer;
import lombok.AllArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CustomerConverter {
    private ModelMapper modelMapper;
    private UserConverter userConverter;

    public Customer fromRegisterDto(RegisterCustomerDto dto) {
         return modelMapper.map(dto, Customer.class);
    }

    public ResponseCustomerDto toResponseDto(Customer customer, UserRepresentation user) {
        var dto=modelMapper.map(customer, ResponseCustomerDto.class);
        if(user!=null) {
            var userDto=userConverter.toDto(user);
            dto.setUser(userDto);
        }
        return dto;
    }
}
