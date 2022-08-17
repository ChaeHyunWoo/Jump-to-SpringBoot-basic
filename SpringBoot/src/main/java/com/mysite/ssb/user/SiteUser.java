package com.mysite.ssb.user;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


// 사용자를 관리하는 SiteUser 엔티티
@Getter
@Setter
@Entity
public class SiteUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // pk

    @Column(unique = true) // 값 중복 방지
    private String username;

    private String password;

    @Column(unique = true) // 값 중복 방지
    private String email;

}