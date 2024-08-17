package com.greb.service;

import com.greb.contraints.UserRole;
import com.greb.dto.Customer.*;
import com.greb.exception.BadRequestException;
import com.greb.model.Customer;
import com.greb.repository.CustomerRepository;
import com.greb.dto.Pagination;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
        var userResource= realmResource.users().get(userId);
        if(userResource==null) throw new BadRequestException("UserId "+userId+" not found");
        List<RoleRepresentation> roles=userResource.roles().realmLevel().listAll();
        if (roles.stream().anyMatch(
                role -> Arrays.stream(UserRole.values())
                        .anyMatch(userRole -> userRole.name().equals(role.getName())
                )
        )){
            throw new BadRequestException("This user has already registered");
        }
        RoleRepresentation customerRole= realmResource.roles().get(UserRole.CUSTOMER.toString()).toRepresentation();
        userResource.roles().realmLevel().add(List.of(customerRole));
        // create new Customer
        var customer= customerConverter.fromRegisterDto(dto);
        customer.setUserId(userId);
        customer.setDiscountPoint(0);
        var savedCustomer=customerRepo.save(customer);

        return customerConverter.toResponseDto(savedCustomer, userResource.toRepresentation());
    }

    @Transactional
    public ResponseCustomerDto updateProfile(UpdateCustomerDto dto){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        var user= realmResource.users().get(userId).toRepresentation();
        if(user==null) throw new BadRequestException("UserId "+userId+" not found");

        // update customer
        var customer= customerRepo.findByUserId(userId);
        if(customer==null) throw new BadRequestException("No customer found with the userId "+userId);
        customer.setAddress(dto.getAddress());
        customer.setGender(dto.getGender());
        customer.setDateOfBirth(dto.getDateOfBirth());
        customer.setPhoneNumber(dto.getPhoneNumber());
        customer.setName(dto.getName());
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

    public ListCustomersDto searchCustomers(String name, Integer pageNo, Integer pageSize){
        PageRequest pageRequest= PageRequest.of(pageNo, pageSize);
        Page<Customer> customerPage= customerRepo.searchCustomers(name.toLowerCase(), pageRequest);
        var customerDtos= customerPage.getContent().stream()
                .map(customer-> customerConverter.toResponseDto(customer, null))
                .toList();
        var pagination= new Pagination(pageNo, customerPage.getTotalPages(), customerPage.getTotalElements());
        return new ListCustomersDto(customerDtos, pagination);
    }
}
