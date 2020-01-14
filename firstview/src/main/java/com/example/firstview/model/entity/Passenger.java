package com.example.firstview.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor //생성자
@NoArgsConstructor //모든 매개변수를 포함한 생성자
@Table(name="passenger")
@Builder
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String kakaoToken;

    private String kakaoId;

    private String profileImage;

    private String name;

    private String phoneNumber;

    private String email;

    private String startPlace;

    private Integer isMatch;

}
