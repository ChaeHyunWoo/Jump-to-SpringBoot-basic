package com.mysite.ssb.question;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


// 질문 등록 빈칸검증
// - 화면에서 전달되는 입력 값을 검증하기 위해서는 폼 클래스가 필요
//   --> QuestionForm 클래스를 컨트롤러에서 사용할 수 있게 해야함 -> QuestionController 수정
@Getter
@Setter
public class QuestionForm {

    // @NotEmpty : Null 또는 빈 문자열("")을 허용하지 않는다.
    // @Size : 문자 길이를 제한한다.

    @NotEmpty(message = "제목은 필수항목입니다.")
    @Size(max = 200) // 길이가 200이 넘으면 오류 발생
    private String subject;

    @NotEmpty(message = "내용은 필수항목입니다.")
    private String content;
}
