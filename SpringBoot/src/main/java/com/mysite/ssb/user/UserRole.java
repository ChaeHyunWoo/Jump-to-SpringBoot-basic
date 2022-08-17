package com.mysite.ssb.user;

import lombok.Getter;

// admin, user 2개의 권한을 갖는 UserRole (enum은 열거 자료형)
// - 스프링 시큐리티는 인증 뿐만 아니라 권한도 관리한다. -> 따라서 잉ㄴ증 후에 사용자에게 부여할 권한이 필요함
@Getter
public enum UserRole {
    ADMIN("ROLE_ADMIN"), // ADMIN은 "ROLE_ADMIN" 라는 값을 가짐
    USER("ROLE_USER");   // USER은 "ROLE_USER" 라는 값을 가짐

    UserRole(String value) {
        this.value = value;
    }

    private String value;
}
