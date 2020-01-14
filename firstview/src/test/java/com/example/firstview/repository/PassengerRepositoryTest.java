package com.example.firstview.repository;


import com.example.firstview.FirstviewApplicationTests;
import com.example.firstview.model.entity.Passenger;
import com.example.firstview.service.KakaoAPI;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PassengerRepositoryTest extends FirstviewApplicationTests {

    private String client_id = "1d898da97758eca206989c9aa2654296"; //내 앱의 REST API key


    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private KakaoAPI kakaoAPI;

    @Test
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
