package com.example.firstview.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.web.bind.annotation.PathVariable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.print.attribute.standard.DateTimeAtCompleted;

@Entity
@Data
@AllArgsConstructor //생성자
@NoArgsConstructor //모든 매개변수를 포함한 생성자
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String kakaoId;

    private String profileImage;

    private String name;

    private String startPlace;

    private DateTimeAtCompleted startTime;

    private Integer carNumber;

    private Integer carPassenger;

    private Integer isMatch;

    private Integer isStart;


}
