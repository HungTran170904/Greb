package com.greb.service;

import com.greb.Contraints.UserRole;
import com.greb.Exception.BadRequestException;
import com.greb.Repository.CustomerRepository;
import com.greb.dto.Customer.CustomerConverter;
import com.greb.dto.Customer.RegisterCustomerDto;
import com.greb.dto.Customer.ResponseCustomerDto;
import com.greb.dto.Customer.UpdateCustomerDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepo;
    private final CustomerConverter customerConverter;
    private final RealmResource realmResource;

    @Transactional
    public ResponseCustomerDto register(RegisterCustomerDto dto){
        String userId= SecurityContextHolder.getContext().getAuthentication().getName();
        // add CUSTOMER role
        var user= realmResource.users().get(userId);
        if(user==null) throw new BadRequestException("UserId "+userId+" not found");
        List<RoleRepresentation> roles=user.roles().realmLevel().listAll();
        if (roles.stream().anyMatch(
                role -> Arrays.stream(UserRole.values())
                        .anyMatch(userRole -> userRole.name().equals(role.getName())
                )
        )){
            throw new BadRequestException("This user has already registered");
        }
        RoleRepresentation customerRole= realmResource.roles().get(UserRole.CUSTOMER.toString()).toRepresentation();
        user.roles().realmLevel().add(List.of(customerRole));
        // create new Customer
        var customer= customerConverter.fromRegisterDto(dto);
        customer.setUserId(userId);
        customer.setDiscountPoint(0);
        customerRepo.save(customer);

        return customerConverter.toResponseDto(customer, user.toRepresentation());
    }

    @Transactional
    public ResponseCustomerDto updateProfile(UpdateCustomerDto dto){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        //update user in keycloak
        var user= realmResource.users().get(userId).toRepresentation();
        if(user==null) throw new BadRequestException("UserId "+userId+" not found");
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        realmResource.users().get(userId).update(user);

        // update customer
        var customer= customerRepo.findByUserId(userId);
        if(customer==null) throw new BadRequestException("No customer found with the userId "+userId);
        customer.setAddress(dto.getAddress());
        customer.setGender(dto.getGender());
        customer.setDateOfBirth(dto.getDateOfBirth());
        customer.setPhoneNumber(dto.getPhoneNumber());
        customerRepo.save(customer);

        return customerConverter.toResponseDto(customer, user);
    }

    public void updateDiscountPoint(Integer discountPoint){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        customerRepo.updateDiscountPoint(userId, discountPoint);
    }

    public ResponseCustomerDto getProfile(){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        var user= realmResource.users().get(userId).toRepresentation();
        if(user==null) throw new BadRequestException("UserId "+userId+" not found");
        var customer= customerRepo.findByUserId(userId);
        if(customer==null) throw new BadRequestException("No customer found with the userId "+userId);
        return customerConverter.toResponseDto(customer, user);
    }
}
