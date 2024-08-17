package com.greb.service;

import com.greb.contraints.UserRole;
import com.greb.dto.Driver.*;
import com.greb.exception.BadRequestException;
import com.greb.model.Driver;
import com.greb.repository.DriverRepository;
import com.greb.model.enums.JobStatus;
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
public class DriverService {
    private final RealmResource realmResource;
    private final DriverConverter driverConverter;
    private final DriverRepository driverRepo;
    private final DriverRepository driverRepository;

    @Transactional
    public ResponseDriverDto register(RegisterDriverDto dto) {
        String userId= SecurityContextHolder.getContext().getAuthentication().getName();
        // add DRIVER role
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
        RoleRepresentation customerRole= realmResource.roles().get(UserRole.DRIVER.toString()).toRepresentation();
        user.roles().realmLevel().add(List.of(customerRole));

        // create new driver
        var driver= driverConverter.fromRegisterDto(dto);
        driver.setUserId(userId);
        driver.setJobStatus(JobStatus.INACTIVE);
        driver.setTotalRatingPoints(0);
        driver.setTotalRatings(0);
        var savedDriver=driverRepo.save(driver);

        return driverConverter.toResponseDto(savedDriver, user.toRepresentation());
    }

    public ResponseDriverDto updateProfile(UpdateDriverDto dto){
        String userId=SecurityContextHolder.getContext().getAuthentication().getName();
        var user= realmResource.users().get(userId).toRepresentation();
        if(user==null) throw new BadRequestException("User not found");

        var driver= driverRepository.findByUserId(userId);
        driver.setAddress(dto.getAddress());
        driver.setPhoneNumber(dto.getPhoneNumber());
        driver.setDateOfBirth(dto.getDateOfBirth());
        driver.setLicenceNumber(dto.getLicenceNumber());
        driver.setIdentityCard(dto.getIdentityCard());
        driver.setName(dto.getName());
        driverRepo.save(driver);

        return driverConverter.toResponseDto(driver, user);
    }

    public void acceptDriver(String driverId, JobStatus jobStatus){
        var driver= driverRepo.findById(driverId).orElseThrow(()->new BadRequestException("Driver not found"));
        driver.setJobStatus(jobStatus);
        driverRepo.save(driver);
        if(jobStatus==JobStatus.ACTIVE){
            var user= realmResource.users().get(driver.getUserId()).toRepresentation();
            /*mailService.sendMail(
                    user.getEmail(),
                    "Driver accepted",
                    "Hello <b>"+user.getFirstName()+" "+user.getLastName()+"</b>,"+
                            " we are glad to inform you that your driver registration has been accepted."+
                            "Now, you are a member of our <b>Greb</b> family."
            );*/
        }
    }

    public void updateAvarageRating(String driverId, Integer ratingPoint){
        var driver= driverRepo.findById(driverId).orElseThrow(()->new BadRequestException("Driver not found"));
        driver.setTotalRatings(driver.getTotalRatings()+1);
        driver.setTotalRatingPoints(driver.getTotalRatingPoints()+ratingPoint);
        driverRepo.save(driver);
    }

    public ResponseDriverDto getProfile(){
        String userId=SecurityContextHolder.getContext().getAuthentication().getName();
        var user= realmResource.users().get(userId).toRepresentation();
        if(user==null) throw new BadRequestException("User not found");
        var driver= driverRepo.findByUserId(userId);
        if(driver==null) throw new BadRequestException("Driver not found");
        return driverConverter.toResponseDto(driver, user);
    }

    public ListDriversDto searchDrivers(
            String name,
            JobStatus jobStatus,
            Integer pageNo,
            Integer pageSize){
        PageRequest pageRequest=PageRequest.of(pageNo, pageSize);
        Page<Driver> driverPage= driverRepo.searchDrivers(name.toLowerCase(),jobStatus,pageRequest);
        var driverDtos= driverPage.getContent().stream()
                .map(driver-> driverConverter.toResponseDto(driver, null))
                .toList();
        Pagination pagination= new Pagination(pageNo, driverPage.getTotalPages(), driverPage.getTotalElements());
        return new ListDriversDto(driverDtos, pagination);
    }

    public String getDriverId(){
        var userId=SecurityContextHolder.getContext().getAuthentication().getName();
        return driverRepo.findIdByUserId(userId);
    }
}
