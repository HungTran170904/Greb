package com.greb.service;

import com.greb.Contraints.UserRole;
import com.greb.Exception.BadRequestException;
import com.greb.Repository.DriverRepository;
import com.greb.dto.Driver.DriverConverter;
import com.greb.dto.Driver.RegisterDriverDto;
import com.greb.dto.Driver.ResponseDriverDto;
import com.greb.dto.Driver.UpdateDriverDto;
import com.greb.dto.User.UserConverter;
import com.greb.model.enums.JobStatus;
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
public class DriverService {
    private final RealmResource realmResource;
    private final DriverConverter driverConverter;
    private final UserConverter userConverter;
    private final DriverRepository driverRepo;
    private final DriverRepository driverRepository;
    private final MailService mailService;

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
        driverRepo.save(driver);

        return driverConverter.toResponseDto(driver, user.toRepresentation());
    }

    public ResponseDriverDto updateProfile(UpdateDriverDto dto){
        String userId=SecurityContextHolder.getContext().getAuthentication().getName();

        var user= realmResource.users().get(userId).toRepresentation();
        if(user==null) throw new BadRequestException("User not found");
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        realmResource.users().get(userId).update(user);

        var driver= driverRepository.findByUserId(userId);
        driver.setAddress(dto.getAddress());
        driver.setPhoneNumber(dto.getPhoneNumber());
        driver.setDateOfBirth(dto.getDateOfBirth());
        driver.setLicenceNumber(dto.getLicenceNumber());
        driver.setIdentityCard(dto.getIdentityCard());
        driverRepo.save(driver);

        return driverConverter.toResponseDto(driver, user);
    }

    public void acceptDriver(String driverId, JobStatus jobStatus){
        var driver= driverRepo.findById(driverId).orElseThrow(()->new BadRequestException("Driver not found"));
        driver.setJobStatus(jobStatus);
        driverRepo.save(driver);
        if(jobStatus==JobStatus.ACTIVE){
            var user= realmResource.users().get(driver.getUserId()).toRepresentation();
            mailService.sendMail(
                    user.getEmail(),
                    "Driver accepted",
                    "Hello <b>"+user.getFirstName()+" "+user.getLastName()+"</b>,"+
                            " we are glad to inform you that your driver registration has been accepted."+
                            "Now, you are a member of our <b>Greb</b> family."
            );
        }
    }

    public ResponseDriverDto getProfile(){
        String userId=SecurityContextHolder.getContext().getAuthentication().getName();
        var user= realmResource.users().get(userId).toRepresentation();
        if(user==null) throw new BadRequestException("User not found");
        var driver= driverRepo.findByUserId(userId);
        if(driver==null) throw new BadRequestException("Driver not found");
        return driverConverter.toResponseDto(driver, user);
    }
}
