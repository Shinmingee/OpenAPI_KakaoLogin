package com.example.firstview.service;

import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.http.NameValuePair;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.Collection;
import java.nio.charset.Charset;
import java.io.UnsupportedEncodingException;

@Service
public class KakaoAPI {
    private String client_id = "1d898da97758eca206989c9aa2654296"; //내 앱의 REST API key

    public JsonNode getAccessToken(String authorize_code){
        final String RequestUrl = "https://kauth.kakao.com/oauth/token";
        String access_token = "";
        String refresh_token = "";

        final Collection<NameValuePair> postParams = new ArrayList<NameValuePair>();
        //HttpClient 컴포넌트는 클라언트에서 서버로 데이터를 전송하기위해 NameValuePair 인터페이스와 URIBuilder 클래스를 제공하고 있다.
        //NameValuePair 인터페이스는 POST방식에서 사용되며, URIBuilder 클래스는 GET 방식에 사용된다.

        postParams.add(new BasicNameValuePair("grant_type","authorization_code"));
        postParams.add(new BasicNameValuePair("client_id", client_id));
        postParams.add(new BasicNameValuePair("redirect_uri","http://localhost:8080/oauth"));
        postParams.add(new BasicNameValuePair("code", authorize_code));

        final HttpClient client = HttpClientBuilder.create().build();
        final HttpPost post = new HttpPost(RequestUrl);

        JsonNode returnNode = null;

        try {
            post.setEntity(new UrlEncodedFormEntity(postParams));
            final HttpResponse response = client.execute(post);
            // HttpClient에서 카카오 로그인을 담은 HttpPost를 HttpResponse에 담어
            //

            ObjectMapper mapper = new ObjectMapper();
            returnNode = mapper.readTree(response.getEntity().getContent());

            access_token = returnNode.get("access_token").toString();
            refresh_token = returnNode.get("refresh_token").toString();

            System.out.print("returnNode 값"+returnNode);
            //returnNode 예상 값.

            //HTTP/1.1 200 OK
            //Content-Type: application/json;charset=UTF-8
            //{
            //    "access_token":"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
            //        "token_type":"bearer",
            //        "refresh_token":"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy",
            //        "expires_in":43199,
            //        "scope":"Basic_Profile"
            //}
            //요청 결과로 API를 호출할 때 사용하는 access_token과 해당 토큰의 만료 시간(초 단위),
            //또한 토큰을 갱신 할 수 있는 refresh_token을 받습니다.
            //사용자 토큰 갱신

            System.out.println("access_token = " + access_token);
            System.out.println("refresh_token = " + refresh_token);


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally {

        }
        return returnNode;
    }

    //받아온 access_Token 을 이용해서 사용자 정보를 받아오는 부분
    public JsonNode getUserInfo(JsonNode access_token)
    {
        final String RequestUrl = "https://kapi.kakao.com/v2/user/me";

        final HttpClient client = HttpClientBuilder.create().build();
        final HttpPost post = new HttpPost(RequestUrl);

        JsonNode resultNode = null;
        post.addHeader("Authorization", "Bearer " + access_token);

        try {
            final HttpResponse response = client.execute(post);
            ObjectMapper mapper = new ObjectMapper();

            System.out.println("response.getEntity() : "+response.getEntity());
            //response.getEntity()
            // : ResponseEntityProxy{[Content-Type: application/json;charset=UTF-8,Content-Length: 718,Chunked: false]}
            System.out.println("response.getEntity().getContent() : "+response.getEntity().getContent());
            //response.getEntity().getContent() : org.apache.http.conn.EofSensorInputStream@2b59d59f

            resultNode = mapper.readTree(response.getEntity().getContent());

            System.out.println("resultNode : "+resultNode);
            //resultNode : {
            //0              "id":1245647089,
            //1              "connected_at":"2019-12-27T06:37:28Z",
            //2              "properties":{
            // 2-1                          "nickname":"신민지",
            // 2-2                          "profile_image":"http://k.kakaocdn.net/dn/clp6Cu/btqAwIduPs6/kKvmNAlnWiV1aHK7Grgj21/img_640x640.jpg",
            // 2-3                          "thumbnail_image":"http://k.kakaocdn.net/dn/clp6Cu/btqAwIduPs6/kKvmNAlnWiV1aHK7Grgj21/img_110x110.jpg"
            //                              },
            //3              "kakao_account":{
            // 3-1                            "profile_needs_agreement":false,
            // 3-2                            "profile":{
            //  3-2-1                                    "nickname":"신민지",
            //  3-2-2                                    "thumbnail_image_url":"http://k.kakaocdn.net/dn/clp6Cu/btqAwIduPs6/kKvmNAlnWiV1aHK7Grgj21/img_110x110.jpg",
            //  3-2-3                                    "profile_image_url":"http://k.kakaocdn.net/dn/clp6Cu/btqAwIduPs6/kKvmNAlnWiV1aHK7Grgj21/img_640x640.jpg"
            //                                          },
            // 3-3                            "has_email":true,
            // 3-4                            "email_needs_agreement":false,
            // 3-5                            "is_email_valid":true,
            // 3-6                            "is_email_verified":true,
            // 3-7                            "email":"mgshin101@kakao.com"
            //                              }
            //               }



        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally {

        }
        return resultNode;
    }
    //https://kapi.kakao.com/v2/user/me


}


//        try{
//            URL url = new URL(requestUrl);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//            //POST 요청을 위해 기본값이 false인 setDoOutput을 true로
//            conn.setRequestMethod("POST");
//            conn.setDoOutput(true);
//
//            //POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
//            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
//            StringBuilder sb = new StringBuilder();
//
//            sb.append("grant_type=authorization_code");
//            sb.append("&client_id="+client_id);
//            sb.append("&redirect_uri=http://localhost:8080/oauth");
//            sb.append("&code="+authorize_code);
//
//            bw.write(sb.toString());
//            bw.flush();
//
//            //결과 코드가 200이라면 성공
//            int responseCode = conn.getResponseCode();
//            System.out.println("responseCode : " + responseCode);
//
//            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
//            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            String line = "";
//            String result = "";
//
//            while ((line = br.readLine()) != null) {
//                result += line;
//            }
//            System.out.println("response body : " + result);
//
//            //    Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
//            JsonParser parser = new JsonParser();
//            JsonElement element = parser.parse(result);
//
//            accessToken = element.getAsJsonObject().get("accessToken").getAsString();
//            refreshToken = element.getAsJsonObject().get("refreshToken").getAsString();
//
//            System.out.println("access_token : " + accessToken);
//            System.out.println("refresh_token : " + refreshToken);
//
//            br.close();
//            bw.close();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        return accessToken;
//    }
