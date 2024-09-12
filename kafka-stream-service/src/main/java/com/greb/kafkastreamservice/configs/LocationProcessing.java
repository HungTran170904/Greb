package com.greb.kafkastreamservice.configs;

import com.greb.kafkastreamservice.constraints.KafkaTopics;
import com.greb.kafkastreamservice.dtos.DriverStatus;
import com.greb.kafkastreamservice.dtos.InputLocationDto;
import com.greb.kafkastreamservice.dtos.OutputLocationDto;
import com.greb.kafkastreamservice.serdes.JsonSerde;
import com.greb.kafkastreamservice.utils.PositionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LocationProcessing {
    @Autowired
    private PositionUtil positionUtil;

    @Autowired
    void buildPipeLine(StreamsBuilder streamBuilder) {
        KStream<String, InputLocationDto> inputKStream = streamBuilder.stream(
                KafkaTopics.inputLocationTopic,
                Consumed.with(Serdes.String(), new JsonSerde(InputLocationDto.class))
        );
        KTable<String, String> rideEventKTable = streamBuilder.stream(
                KafkaTopics.rideEventTopic,
                Consumed.with(Serdes.String(), Serdes.String())
        ).toTable();

        KTable<String, OutputLocationDto> outputKTable = inputKStream
                .leftJoin(rideEventKTable, (inputLocation, statusStr)->{
                    try{
                        var status= DriverStatus.valueOf(statusStr);
                        inputLocation.setStatus(status);
                    }
                    catch(Exception e){
                        log.error("Cannot parse driver status: " + statusStr);
                    }
                    return inputLocation;
                })
                .groupByKey()
                .aggregate(
                        ()->new OutputLocationDto(),
                        (key,inputLocation,outputLocation)->{
                            try {
                                var newOutputLocation = OutputLocationDto.builder()
                                        .driverId(key)
                                        .position(inputLocation.getPosition())
                                        .serviceTypeId(inputLocation.getServiceTypeId())
                                        .vehicleId(inputLocation.getVehicleId())
                                        .status(inputLocation.getStatus())
                                        .timestamp(inputLocation.getTimestamp())
                                        .build();
                                if (outputLocation.getPosition() == null) return newOutputLocation;

                                var twoPointsDistance = positionUtil.countDistance(
                                        inputLocation.getPosition(),
                                        outputLocation.getPosition());
                                double instantSpeed = twoPointsDistance * 60 * 60 * 1000.0 /
                                        (inputLocation.getTimestamp() - outputLocation.getTimestamp());
                                var bearing = positionUtil.countBearing(
                                        inputLocation.getPosition(),
                                        outputLocation.getPosition());

                                newOutputLocation.setBearing(bearing);
                                newOutputLocation.setInstantSpeed(instantSpeed);
                                if (newOutputLocation.getStatus()==DriverStatus.ACTIVE) {
                                    newOutputLocation.setRideDistance(
                                            outputLocation.getRideDistance() + instantSpeed
                                    );
                                } else newOutputLocation.setRideDistance(0.0);

                                return newOutputLocation;
                            }
                            catch(Exception e) {
                                log.error("Kakfa error: {}", e.getMessage());
                                return outputLocation;
                            }
                        },
                        Materialized.with(Serdes.String(),new JsonSerde(OutputLocationDto.class))
                );

        outputKTable.toStream().to(KafkaTopics.outputLocationTopic);
    }
}
