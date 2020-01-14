package com.example.firstview.service;

import com.example.firstview.model.entity.Passenger;
import com.example.firstview.repository.PassengerRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PassengerService{

    private String client_id = "1d898da97758eca206989c9aa2654296"; //내 앱의 REST API key

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private KakaoAPI kakaoAPI;

    //새로운 정보 저장
    public void crate(){

        JsonNode userInfo = kakaoAPI.getUserInfo(kakaoAPI.getAccessToken(client_id));

        if(!userInfo.get("id").toString().isEmpty()){

            JsonNode properties = userInfo.path("properties");
            JsonNode kakao_account = userInfo.path("kakao_account");

            Passenger passenger = Passenger.builder()
                    .kakaoId(userInfo.get("id").toString())
                    .name(properties.get("nickname").asText())
                    .email(kakao_account.get("email").asText())
                    .profileImage(properties.get("profile_image").asText())
                    .build();

            Passenger newPs = passengerRepository.save(passenger);

            System.out.println("newPs: "+newPs);

        }

    }

}


