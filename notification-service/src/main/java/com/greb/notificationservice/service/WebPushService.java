package com.greb.notificationservice.service;

import com.google.firebase.messaging.*;
import com.greb.notificationservice.dto.NoticeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebPushService {
    private final FirebaseMessaging firebaseMessaging;

    public BatchResponse sendNotification(NoticeDto dto){
        var registrationTokens=dto.getRegistrationTokens();
        Notification notification= Notification.builder()
                .setTitle(dto.getTitle())
                .setBody(dto.getContent())
                .setImage(dto.getImage())
                .build();
        MulticastMessage multicastMessage= MulticastMessage.builder()
                .addAllTokens(registrationTokens)
                .setNotification(notification)
                .build();

        BatchResponse batchResponse=null;
        try{
            batchResponse = firebaseMessaging.sendMulticast(multicastMessage);
        }
        catch(FirebaseMessagingException e){
            log.error("Firebase Exception {}", e.getMessage());
        }
        if(batchResponse!=null&&batchResponse.getFailureCount()>0){
            List<SendResponse> responses = batchResponse.getResponses();
            List<String> failedTokens= new ArrayList();
            for(int i=0; i<responses.size();i++){
                if(!responses.get(i).isSuccessful()){
                    failedTokens.add(registrationTokens.get(i));
                }
            }
            log.error("List of failed tokens: {}", failedTokens);
        }
        return batchResponse;
    }
}
