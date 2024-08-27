package com.greb.locationservice.configs;

import com.greb.locationservice.dtos.InputLocation;
import com.greb.locationservice.models.OutputLocation;
import com.greb.locationservice.serdes.JsonSerde;
import com.greb.locationservice.utils.PositionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class LocationProccessor {
    @Autowired
    private PositionUtil positionUtil;

    @Autowired
    void buildPipeLine(StreamsBuilder streamBuilder){
        KStream<String, InputLocation> inputStream= streamBuilder.stream(
                "input-location",
                Consumed.with(Serdes.String(), new JsonSerde(InputLocation.class))
        );
        Aggregator<String, InputLocation, OutputLocation> locationAggr=
                (key, inputLocation,outputLocation)->{
                    System.out.println("Curr location: " + inputLocation.toString());
                    var currPosition= inputLocation.getPosition();
                    var prePosition= outputLocation.getCurrPosition();
                    var currTime= inputLocation.getTime();
                    var preTime= outputLocation.getTime();
                    Double instantSpeed=0.0, rideDistance=0.0;

                    if(!preTime.before(currTime)){
                        log.error("Time of input location is before that of output location");
                        log.error("Input location: " + inputLocation.toString());
                        log.error("Output location: " + outputLocation.toString());
                    }
                    else if(prePosition!=null&&inputLocation.getRideId()!=null){
                        var distance= positionUtil.countDistance(currPosition, prePosition);
                        rideDistance= outputLocation.getRideDistance()+distance;
                        double time= (currTime.getTime()-preTime.getTime())/(60*60*1000.0);
                        instantSpeed=distance/time;
                    }

                    var newOutputLocation= new OutputLocation(
                            key,
                            prePosition,
                            currPosition,
                            inputLocation.getRideId(),
                            inputLocation.getServiceId(),
                            inputLocation.getTime(),
                            instantSpeed,
                            rideDistance
                    );

                    return newOutputLocation;
                };
        var initOutputLocation= new OutputLocation(null,null, null, null, null,new Date(), 0.0, 0.0);
        KTable<String, OutputLocation> outputLocation= inputStream
                .groupByKey()
                .aggregate(
                        ()->initOutputLocation,
                        locationAggr,
                        Materialized.with(Serdes.String(),new JsonSerde(OutputLocation.class))
                );
        outputLocation.toStream().to("output-location");
    }
}
