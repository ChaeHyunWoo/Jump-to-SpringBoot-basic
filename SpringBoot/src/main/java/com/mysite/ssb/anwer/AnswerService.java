package com.mysite.ssb.anwer;

import com.mysite.ssb.DataNotFoundException;
import com.mysite.ssb.question.Question;
import com.mysite.ssb.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

// 답변(anwer)를 저장하는 Service

@RequiredArgsConstructor
@Service
public class AnswerService {

    private final AnswerRepository answerRepository;

    // 입력으로 받은 question(질문), content(내용)을 매개 변수로 받음
    public Answer create(Question question, String content, SiteUser author) {

        Answer answer = new Answer(); // Answer 객체 생성
        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now()); // Local 현재 시간
        answer.setQuestion(question);
        answer.setAuthor(author);
        this.answerRepository.save(answer);

        return answer; // 답변 객체를 반환
    }

    // ID로 답변 조회
    public Answer getAnswer(Integer id) {
        Optional<Answer> answer = this.answerRepository.findById(id);
        if (answer.isPresent()) {
            return answer.get();
        } else {
            throw new DataNotFoundException("answer not found");
        }
    }

    // 답변 수정
    public void modify(Answer answer, String content) {
        answer.setContent(content);
        answer.setModifyDate(LocalDateTime.now());
        this.answerRepository.save(answer);
    }

    // 답변 삭제
    public void delete(Answer answer) {
        this.answerRepository.delete(answer);
    }

    // 답변 추천인 저장
    public void vote(Answer answer, SiteUser siteUser) {
        answer.getVoter().add(siteUser);
        this.answerRepository.save(answer);
    }



}
