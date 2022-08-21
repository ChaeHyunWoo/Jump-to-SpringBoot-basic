package com.mysite.ssb.question;

import com.mysite.ssb.DataNotFoundException;
import com.mysite.ssb.anwer.Answer;
import com.mysite.ssb.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service // 이 클래스를 서비스로 인식한다.
public class QuestionService {

    private final QuestionRepository questionRepository;

    // 질문 검색
    // - JPA를 사용하면 SQL 쿼리문을 Java 코드로 작성 가능
    // - JPA에서 제공하는 Specification 인터페이스 사용 -> 정교한 쿼리의 작성을 도와주는 JPA의 도구
    private Specification<Question> search(String kw) {

        return new Specification<Question>() {
            private static final long serialVersionUID = 1L;

            @Override // Specification 클래스 import 시 오버라이드
            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true); // 중복 제거
                Join<Question,SiteUser> u1 = q.join("author", JoinType.LEFT);
                Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
                Join<Answer,SiteUser> u2 = a.join("author", JoinType.LEFT);

                return
                        cb.or(cb.like(q.get("subject"),"%" + kw + "%"), // 제목
                        cb.like(q.get("content"),"%" + kw + "%"),       // 내용
                        cb.like(u1.get("username"), "%" + kw + "%"),    // 질문 작성자
                        cb.like(a.get("content"), "%" + kw + "%"),      // 답변 내용
                        cb.like(u2.get("username"), "%" + kw + "%"));   // 답변 작성자

            } // end toPredicate()
        }; // end Specification()
    }


    // 페이징 기능
    // 질문 목록을 조회하여 리턴하는 getList 메서드
    // + 검색 기능을 추가하였기에 매개변수에 String kw 추가 - 2022-08-21
    public Page<Question> getList(int page, String kw) {

        // 3-02(페이징) 가장 최근에 작성한 게시물이 가장 먼저 보이도록 구현
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc(("createDate"))); // 날짜 순으로 내림차순 정릴
        // PageRequest.of() :
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<Question> spec = search(kw); // 검색 기능 추가

        // Specification 대신 @Query 사용할 때
        return this.questionRepository.findAllByKeyword(kw, pageable);
        // -> Specification 사용할 때 return this.questionRepository.findAll(spec, pageable);
    }

    // 질문 상세 - 제목 + 내용 출력
    public Question getQuestion(Integer id) {
        Optional<Question> question = this.questionRepository.findById(id);
        if(question.isPresent()) {
            return question.get();
        } else {
            throw new DataNotFoundException("question not fount");
        }
    }

    // 질문 등록(저장) - 제목 + 내용
    // - 제목과 내용을 입력하여 질문 데이터를 저장
    public void create(String subject, String content, SiteUser user) {
        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setCreateDate(LocalDateTime.now());
        q.setAuthor(user);
        this.questionRepository.save(q);
    }

    // 질문 수정
    public void modify(Question question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());
        this.questionRepository.save(question); // 수정한 데이터 저장
    }

    // 질문 삭제
    public void delete(Question question) {
        this.questionRepository.delete(question);
    }

    // 질문 추천인 저장
    public void vote(Question question, SiteUser siteUser) {
        question.getVoter().add(siteUser);
        this.questionRepository.save(question);
    }


}
