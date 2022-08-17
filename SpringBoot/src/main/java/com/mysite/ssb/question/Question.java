package com.mysite.ssb.question;

import com.mysite.ssb.anwer.Answer;
import com.mysite.ssb.user.SiteUser;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

// lombok lib를 사용한 getter/setter
@Getter
@Setter
@Entity // @Entity를 사용하여 JPA가 이 클래스를 엔티티로 인식함.
public class Question { // Quesetion(질문) 엔티티

    @Id // id속성을 기본 키(primary key)로 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 적용하면 데이터를 저장할 때 해당 속성에 값을 따로 세팅하지 않아도 1씩 자동으로 증가하여 저장
    // strategy는 고유번호를 생성하는 옵션으로 GenerationType.IDENTITY는 해당 컬럼만의 독립적인 시퀀스를 생성하여 번호를 증가시킬 때 사용
    private Integer id; // 질문의 고유 번호(pk)

    @Column(length = 200) //엔티티의 속성은 테이블의 컬럼명과 일치하는데 컬럼의 세부 정보를 위해 @Column 어노테이션 사용
    private String subject; // 질문의 제목

    // length는 컬럼의 길이를 설정할때 사용하고 columnDefinition은 컬럼의 속성을 정의할 때 사용한다.
    @Column(columnDefinition = "TEXT") // columnDefinition = "TEXT"은 "내용"처럼 글자 수를 제한할 수 없는 경우에 사용
    private String content; // 질문의 내용

    private LocalDateTime createDate; // 질문 작성일시

    // Answer 엔티티 객체로 구성된 answerList를 속성으로 추가하고 @OneToMany 애너테이션을 설정
    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;

    @ManyToOne // author 속성은 SiteUser엔티티와 @ManyToOne 적용
    private SiteUser author; // -> 여러개의 질문이 한명의 사용자에게 작성될 수 있으므로

    private LocalDateTime modifyDate; // 질문 수정 일시

    @ManyToMany
    Set<SiteUser> voter; // 질문 추천인 - List대신 Set을 사용한 이유는 추천인은 중복 x

    // 질문 객체(예:question)에서 답변을 참조하려면 question.getAnswerList()를 호출하면 된다.
    // @OneToMany 애너테이션에 사용된 mappedBy는 참조 엔티티의 속성명을 의미한다.
    // 즉, Answer 엔티티에서 Question 엔티티를 참조한 속성명 question을 mappedBy에 전달해야 한다.

    // 추가로 질문 하나에 여러개의 답변이 작성될 수 있다.
    // 이때 질문을 삭제하면 질문에 달린 답변들도 모두 삭제하기 위해 @OneToMany의 속성으로 casecade = CascadeType.REMOVE 사용
}
