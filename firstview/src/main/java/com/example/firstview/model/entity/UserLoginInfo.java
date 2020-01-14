package com.example.firstview.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor //생성자
@NoArgsConstructor //모든 매개변수를 포함한 생성자
public class UserLoginInfo {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    public String kakaoId;

    public String profileImage;

    public String name;

    public String email;

    public String phoneNumber;

    public Integer isManager;

    public Integer isDriver;

    public Integer isPassenger;


}
