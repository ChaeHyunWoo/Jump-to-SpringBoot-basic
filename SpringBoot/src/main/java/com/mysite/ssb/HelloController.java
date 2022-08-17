package com.mysite.ssb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // 이 클래스가 컨트롤러의 기능을 수행한다는 어노테이션
public class HelloController {

    // RequestMapping 어노테이션은 http://localhost:8080/hello URL 요청이 발생하면 hello 메서드가 실행됨을 의미
    @RequestMapping("/hello")
    @ResponseBody // hello 메서드의 응답 결과가 문자열 그 자체임을 나타내는 어노테이션
    public String hello() {
        return "Hello Spring test";
    }
}
