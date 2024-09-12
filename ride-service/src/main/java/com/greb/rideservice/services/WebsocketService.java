package com.greb.rideservice.services;

import com.greb.rideservice.dtos.Cancel.ResponseCancelDto;
import com.greb.rideservice.dtos.Ride.AcceptRideDto;
import com.greb.rideservice.dtos.Ride.ResponseRideDto;
import com.greb.rideservice.dtos.RideAddress.ChangeRideAddressDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebsocketService {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendRideRequestToDriver(String driverId, ResponseRideDto rideDto) {

    }

    public void notifyRideCancelToCustomer(String customerId, ResponseCancelDto dto){

    }

    public void notifyRideCancelToDriver(String driverId, ResponseCancelDto dto){

    }

    public void notifyRideFinishedToCustomer(String customerId,String rideId){

    }

    public void notifyRideAcceptingToCustomer(String customerId, AcceptRideDto dto){

    }

    public void notifyDriverRideAddressChange(String driverId, ChangeRideAddressDto dto){

    }
}
