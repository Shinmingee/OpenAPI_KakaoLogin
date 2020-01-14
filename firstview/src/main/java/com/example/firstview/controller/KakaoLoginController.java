package com.example.firstview.controller;

import com.example.firstview.service.KakaoAPI;
import com.example.firstview.service.PassengerService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class KakaoLoginController {

    //카카오 API로 로그인 해보기

    // 0. 앱으로 발급받은 key:1d898da97758eca206989c9aa265429를 보낸다.
    // 1. 허가코드를 받아온다.
    // 2. 허가코드로 Access Token & Refresh Token(토큰)을 받아서 인증하기
    // 2-1. https://kauth.kakao.com/oauth/token의 경로로 필수로 요구하는 파라미터를 포함해서 POST방식으로 요청을 하라는 설명
    // 2-2. Request를 통한 Response Body는 JSON객체로 넘어온답니다.
    // 2-3. JSON 객체를 파싱해서 화면에 띄우면 끝.


    //2.
    @RequestMapping("/oauth")
    public String login(@RequestParam("code") String code, Model model, HttpSession session){
        System.out.println("code : "+code);
        //code : SUV-_HqtLwHnIjDEg1QaBVi1I97_tg9a07CUHo8EhaqEQ5naq2YjrwSZfNj7H7jytyUuXQo9d_cAAAFvo2cSpA

        KakaoAPI kakaoAPI = new KakaoAPI();
        JsonNode node = kakaoAPI.getAccessToken(code); // 하면, JsonNode returnNode 나올 거야.

        JsonNode accessToken = node.get("access_token"); //access_token 가져오기.

        //////session 에 담기
            session.setAttribute("token", accessToken.asText());

            JsonNode userInfo = kakaoAPI.getUserInfo(accessToken); //access_token으로 정보가져오기_KakaoAPI resultNode구하기

            String id = userInfo.get("id").toString();
            String nickname = null;
            String thumbnailImage = null;
            String profileImage = null;
            String email = null;

            JsonNode properties = userInfo.path("properties"); //porperties안에 있는 정보들 : nickname, thumnailImage, profileImage
            if (properties.isMissingNode()){

            } else {
                nickname = properties.get("nickname").asText();
                thumbnailImage = properties.get("thumbnail_image").asText();
                profileImage = properties.get("profile_image").asText();

                System.out.println("nickname : " + nickname);

                System.out.println("thumbnailImage : " + thumbnailImage);

                System.out.println("profileImage : " + profileImage);
            }


            JsonNode kakao_account = userInfo.path("kakao_account");//email

            if (kakao_account.get("has_email").asBoolean())
            {
                email = kakao_account.get("email").asText();
                System.out.println("has_email.asBoolean() is true : "+email);
            }
            else
            {
                email = "email 입력해주세요.";
                System.out.println("has_email.asBoolean() is false : "+email);
            }

            session.setAttribute("id", id);
            session.setAttribute("nickname", nickname);
            session.setAttribute("thumbnailImage",thumbnailImage);
            session.setAttribute("profileImage",profileImage);
            session.setAttribute("email",email);
            session.setAttribute("token", accessToken.asText());


//        JsonNode result = kr.sendTextMsgToMe(access_token);

        return "passengerPage";
        //return "choicePage";

    }
}
