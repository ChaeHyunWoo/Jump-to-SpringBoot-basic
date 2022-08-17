package com.mysite.ssb.anwer;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;


// 답변 등록 빈칸 검증 클래스
@Getter
@Setter
public class AnswerForm {

    @NotEmpty(message = "내용은 필수항목입니다!")
    private String content;
}
