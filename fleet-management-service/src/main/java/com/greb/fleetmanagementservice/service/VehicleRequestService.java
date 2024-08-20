package com.greb.fleetmanagementservice.service;

import com.greb.fleetmanagementservice.dto.Notification.MailDto;
import com.greb.fleetmanagementservice.dto.Notification.NoticeDto;
import com.greb.fleetmanagementservice.dto.Pagination;
import com.greb.fleetmanagementservice.dto.RequestHistory.RequestHistoryConverter;
import com.greb.fleetmanagementservice.dto.RequestHistory.RequestHistoryDto;
import com.greb.fleetmanagementservice.dto.Vehicle.VehicleConverter;
import com.greb.fleetmanagementservice.dto.VehicleRequest.*;
import com.greb.fleetmanagementservice.exception.BadRequestException;
import com.greb.fleetmanagementservice.model.RequestHistory;
import com.greb.fleetmanagementservice.model.Vehicle;
import com.greb.fleetmanagementservice.model.VehicleRequest;
import com.greb.fleetmanagementservice.model.enums.RequestStatus;
import com.greb.fleetmanagementservice.model.enums.RequestType;
import com.greb.fleetmanagementservice.repository.RequestHistoryRepository;
import com.greb.fleetmanagementservice.repository.ServiceTypeRepository;
import com.greb.fleetmanagementservice.repository.VehicleRepository;
import com.greb.fleetmanagementservice.repository.VehicleRequestRepository;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.InternalException;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class VehicleRequestService {
    private final VehicleRequestConverter vehicleRequestConverter;
    private final RequestHistoryConverter requestHistoryConverter;
    private final VehicleConverter vehicleConverter;
    private final UserService userService;
    private final VehicleRequestRepository vehicleRequestRepo;
    private final RequestHistoryRepository requestHistoryRepo;
    private final ServiceTypeRepository serviceTypeRepo;
    private final VehicleRepository vehicleRepo;
    private final NotificationService notiService;


    private void notifyVehicleRequestToAdmins(VehicleRequest vehicleRequest, boolean isCreated){
        String title,content;
        if(isCreated){
            title="New Vehicle Request";
            if(vehicleRequest.getRequestType()==RequestType.REGISTER)
                content="A driver want to register a new vehicle with licence plate '"+vehicleRequest.getLicencePlate()+"'";
            else {
                var licencePlate= vehicleRepo.findLicencePlateById(vehicleRequest.getVehicle().getId());
                if(vehicleRequest.getRequestType()==RequestType.UPDATE)
                    content="A driver want to update info of vehicle  with licence plate '"+licencePlate+"'";
                else content="A driver want to unregister the vehicle with licence plate '"+licencePlate+"'";
            }
        }
        else{
            title="Update Vehicle Request";
            content="Vehicle Request has been updated";
        }

        var noticeDto= NoticeDto.builder()
                .title(title)
                .content(content+". VehicleRequestId: "+vehicleRequest.getId())
                .build();
        notiService.sendNotificationToAdmins(noticeDto);
    }

    @Transactional
    public ResVehicleRequestDto create(CreateVehicleRequestDto dto){
        var vehicleRequest= vehicleRequestConverter.fromReqDto(dto);

        if(dto.getRequestType()==RequestType.REGISTER){
            var serviceType= serviceTypeRepo.findById(dto.getServiceTypeId())
                    .orElseThrow(()-> new BadRequestException("Service type does not exist"));
            if(!serviceType.getAcceptedVehicleTypes().contains(dto.getVehicleType()))
                throw new BadRequestException(
                        "Service '"+serviceType.getName()+
                        "' only accept vehicles: "+serviceType.getAcceptedVehicleTypes());
            vehicleRequest.setServiceType(serviceType);
        }
        else {
            if(dto.getVehicleId()==null||!vehicleRepo.existsById(dto.getVehicleId()))
                throw new BadRequestException("Vehicle does not exist");
            vehicleRequest.setVehicle(vehicleRepo.getById(dto.getVehicleId()));
        }

        var driverId= userService.getDriverId();
        vehicleRequest.setDriverId(driverId);
        var savedVehicleRequest= vehicleRequestRepo.save(vehicleRequest);

        // send notification to admins
        notifyVehicleRequestToAdmins(vehicleRequest, true);

        return vehicleRequestConverter.toResponseDto(savedVehicleRequest);
    }

    @Transactional
    public ResVehicleRequestDto update(String vehicleRequestId, UpdateVehicleRequestDto dto){
        var vehicleRequest= vehicleRequestRepo.findById(vehicleRequestId)
                        .orElseThrow(()-> new BadRequestException("Vehicle does not exist"));
        vehicleRequestConverter.updateVehicleRequest(vehicleRequest, dto);
        var savedVehicleRequest= vehicleRequestRepo.save(vehicleRequest);

        //create request history
        String driverId= userService.getDriverId();
        String changeInfo="Update vehicle request";
        var requestHistory= RequestHistory.builder()
                .vehicleRequest(vehicleRequest)
                .comment(dto.getComment())
                .driverId(driverId)
                .changeInfo(changeInfo)
                .build();
        requestHistoryRepo.save(requestHistory);

        // send notification to admins
        notifyVehicleRequestToAdmins(vehicleRequest, false);

        return vehicleRequestConverter.toResponseDto(savedVehicleRequest);
    }

    @Transactional
    public RequestHistoryDto approveRequest(ApprovalDto approvalDto){
        var vehicleRequest= vehicleRequestRepo.findById(approvalDto.vehicleRequestId())
                .orElseThrow(() -> new BadRequestException("Vehicle Request not found"));
        String adminId= SecurityContextHolder.getContext().getAuthentication().getName();

        String changeInfo;
        if(approvalDto.requestStatus()== RequestStatus.APPROVE){
            changeInfo= "Admin has approved the request of vehicle";
            if(vehicleRequest.getRequestType()== RequestType.REGISTER){
                var vehicle= vehicleConverter.fromVehicleRequest(vehicleRequest);
                vehicle.setIsAvailable(true);
                vehicleRepo.save(vehicle);
            }
            else {
                var vehicle = vehicleRepo.findById(vehicleRequest.getVehicle().getId())
                        .orElseThrow(() -> new InternalException("Vehicle Id" + vehicleRequest.getVehicle().getId() + " not found"));
                if (vehicleRequest.getRequestType() == RequestType.UPDATE)
                    vehicleConverter.updateVehicle(vehicle, vehicleRequest);
                else if (vehicleRequest.getRequestType() == RequestType.UNREGISTER)
                    vehicle.setIsAvailable(false);
                vehicleRepo.save(vehicle);
            }
            notiService.sendEmailToDriver(vehicleRequest.getDriverId(), changeInfo);
        }
        else if(approvalDto.requestStatus()== RequestStatus.REJECT){
            changeInfo= "Admin has rejected the vehicle request";
            notiService.sendEmailToDriver(vehicleRequest.getDriverId(), changeInfo);
        }
        else throw new BadRequestException("Invalid request status");

        vehicleRequest.setStatus(approvalDto.requestStatus());
        vehicleRequestRepo.save(vehicleRequest);

        var requestHistory= RequestHistory.builder()
                .vehicleRequest(vehicleRequest)
                .comment(approvalDto.comment())
                .adminId(adminId)
                .changeInfo(changeInfo)
                .build();
        var savedHistory=requestHistoryRepo.save(requestHistory);

        return requestHistoryConverter.toDto(savedHistory);
    }

    public void cancelRequest(String vehicleRequestId){
        var vehicleRequest= vehicleRequestRepo.findById(vehicleRequestId)
                .orElseThrow(() -> new BadRequestException("Vehicle Request not found"));
        if(vehicleRequest.getStatus()==RequestStatus.APPROVE)
            throw new BadRequestException("Vehicle Request has been approved");
        vehicleRequestRepo.delete(vehicleRequest);
        // send notification to admins
        var noticeDto= NoticeDto.builder()
                .title("Cancel vehicle request")
                .content("Vehicle Request has been cancelled. VehicleRequestId: "+vehicleRequestId)
                .build();
        notiService.sendNotificationToAdmins(noticeDto);
    }

    public List<RequestHistoryDto> getRequestHistoriesByVehicleRequestId(String vehicleRequestId){
        return requestHistoryRepo.findByVehicelRequestId(vehicleRequestId)
                .stream().map(rh->requestHistoryConverter.toDto(rh))
                .toList();
    }

    public ListVehicleRequestsDto searchVehicleRequests(
            RequestStatus status,
            RequestType requestType,
            Integer pageNo,
            Integer pageSize
    ){
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
        var vehicleRequestPage= vehicleRequestRepo.searchVehicleRequests(status,requestType, pageRequest);
        var vehicleReuqestDtos=vehicleRequestPage.getContent().stream()
                .map(vr->vehicleRequestConverter.toResponseDto(vr))
                .toList();
        Pagination pagination= new Pagination(pageNo, vehicleRequestPage.getTotalPages(), vehicleRequestPage.getTotalElements());
        return new ListVehicleRequestsDto(vehicleReuqestDtos, pagination);
    }

    public ListVehicleRequestsDto getRequestsByDriverId(
            Integer pageNo,
            Integer pageSize
    ){
        String driverId= userService.getDriverId();
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
        var vehicleRequestPage= vehicleRequestRepo.getByDriverId(driverId, pageRequest);
        var vehicleRequestDtos=vehicleRequestPage.getContent().stream()
                .map(vr->vehicleRequestConverter.toResponseDto(vr))
                .toList();
        var pagination= new Pagination(pageNo, vehicleRequestPage.getTotalPages(), vehicleRequestPage.getTotalElements());
        return new ListVehicleRequestsDto(vehicleRequestDtos, pagination);
    }
}
