package com.greb.fleetmanagementservice.dto.RequestHistory;

import com.greb.fleetmanagementservice.model.RequestHistory;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RequestHistoryConverter {
    private final ModelMapper modelMapper;

    public RequestHistoryDto toDto(RequestHistory requestHistory) {
        var dto= modelMapper.map(requestHistory, RequestHistoryDto.class);
        if(requestHistory.getVehicleRequest()!=null)
            dto.setVehicleRequestId(requestHistory.getVehicleRequest().getId());
        return dto;
    }
}
