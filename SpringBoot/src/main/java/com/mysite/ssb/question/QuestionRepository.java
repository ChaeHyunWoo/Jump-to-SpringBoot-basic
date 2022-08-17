package com.mysite.ssb.question;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Repository - 엔티티에 의해 생성된 DB 테이블에 접근하는 메서드들(ex: findAll, save)등을 사용하기 위한 인터페이스
// 엔티티만으로는 DB에 데이터를 저장하거나 조회 못함 -> 데이터 처리를 위해서는 실제 데이터베이스와 연동하는 JPA repository가 필요

// 이 인터페이스를 리포지터리로 만들기 위해 JpaRepository 인터페이스 상속받음.
// JpaRepoitory를 상속할 때는 제네릭스 타입으로 <Question, Integer> 처럼 리포지터리의 대상이 되는 엔티티의 타입(Question)과
// 해당 엔티티의 PK의 속성 타입(Integer)을 지정해야 한다. 이것은 JpaRepository를 생성하기 위한 규칙
public interface QuestionRepository extends JpaRepository<Question, Integer> {

    // 원래 아무것도 없었는데 findBySubject 사용을 위해 하단 소스 작성
    Question findBySubject(String subject);

    // 제목 + 내용을 조회하기위해 추가
    Question findBySubjectAndContent(String subject, String content);

    // 제목에 특정 문자열이 포함되어 있는 데이터 조회
    List<Question> findBySubjectLike(String subject);

    // 페이징 처리(JPA lib를 받았으면 안에 들어있다.)
    // - Pageable 객체를 입력으로 받아 Page<Question> 타입 객체를 리턴하는 findAll 메서드 생성
    Page<Question> findAll(Pageable pageable);
}
