package com.mysite.ssb.question;

import com.mysite.ssb.DataNotFoundException;
import com.mysite.ssb.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service // 이 클래스를 서비스로 인식한다.
public class QuestionService {

    private final QuestionRepository questionRepository;

    // 페이징 기능
    // 질문 목록을 조회하여 리턴하는 getList 메서드
    public Page<Question> getList(int page) {

        // 3-02(페이징) 가장 최근에 작성한 게시물이 가장 먼저 보이도록 구현
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc(("createDate"))); // 날짜 순으로 내림차순 정릴
        // PageRequest.of() :
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.questionRepository.findAll(pageable);
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
