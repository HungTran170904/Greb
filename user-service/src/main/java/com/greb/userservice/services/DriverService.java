package com.greb.userservice.services;

import com.greb.userservice.contraints.UserRole;
import com.greb.userservice.dtos.Notification.MailDto;
import com.greb.userservice.dtos.Notification.NoticeDto;
import com.greb.userservice.exceptions.BadRequestException;
import com.greb.userservice.models.Driver;
import com.greb.userservice.repositories.DriverRepository;
import com.greb.userservice.models.enums.JobStatus;
import com.greb.userservice.dtos.Driver.*;
import com.greb.userservice.dtos.Pagination;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class DriverService {
    private final RealmResource realmResource;
    private final DriverConverter driverConverter;
    private final NotificationService notiService;
    private final DriverRepository driverRepo;
    private final DriverRepository driverRepository;


    private void notifyNewDriverToAdmin(String driverId){
        final String jwt= ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getTokenValue();
        CompletableFuture.runAsync(()->{
            RoleResource roleResource = realmResource.roles().get("ADMIN");
            List<String> fcmTokens = new ArrayList();
            roleResource.getRoleUserMembers().forEach(member -> {
                if(member.getAttributes().containsKey("fcmToken"))
                    fcmTokens.add(member.getAttributes().get("fcmToken").get(0));
            });
            var noticeDto= NoticeDto.builder()
                    .registrationTokens(fcmTokens)
                    .title("New Driver Registration")
                    .content("There is a new driver registration request. DriverId: "+driverId)
                    .build();
            notiService.sendWebPush(noticeDto, jwt);
        });
    }

    private void notifyAcceptedToDriver(Driver driver){
        final String jwt= ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getTokenValue();
        CompletableFuture.runAsync(()->{
            var user= realmResource.users().get(driver.getUserId()).toRepresentation();
            String content;
            if(driver.getJobStatus()==JobStatus.ACTIVE)
                content="Hello <b>"+user.getFirstName()+" "+user.getLastName()+"</b>,"+
                        " we are glad to inform you that your driver registration has been accepted."+
                        "Now, you are a member of our <b>Greb</b> family.";
            else content="Hello <b>"+user.getFirstName()+" "+user.getLastName()+"</b>,"+
                    " your driver registration has been rejected. " +
                    "For more info, please contact email: <a>tienhung17092004@gmail.com</a>.";
            var mailDto= MailDto.builder()
                    .destEmails(List.of(user.getEmail()))
                    .subject("Driver accepted")
                    .content(content)
                    .build();
            notiService.sendEmail(mailDto, jwt);
        });
    }

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
        driver.setAverageRating(0.0);
        var savedDriver=driverRepo.save(driver);

        notifyNewDriverToAdmin(driver.getId());

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
        /*if(driver.getJobStatus().equals(jobStatus))
            throw new BadRequestException("Driver is already in '"+jobStatus+"' job status");*/
        driver.setJobStatus(jobStatus);
        var savedDriver=driverRepo.save(driver);

        notifyAcceptedToDriver(savedDriver);
    }

    public void updateAvarageRating(String driverId, Double averageRating){
        var driver= driverRepo.findById(driverId).orElseThrow(()->new BadRequestException("Driver not found"));
        driver.setAverageRating(averageRating);
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

    public String getUserIdByDriverId(String driverId){
        return driverRepo.findUserIdById(driverId);
    }

    public JobStatus getJobStatus(String driverId){
        return driverRepo.findJobStatusById(driverId);
    }
}
