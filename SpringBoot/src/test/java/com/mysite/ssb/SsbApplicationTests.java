package com.mysite.ssb;

import com.mysite.ssb.anwer.Answer;
import com.mysite.ssb.anwer.AnswerRepository;
import com.mysite.ssb.question.Question;
import com.mysite.ssb.question.QuestionRepository;
import com.mysite.ssb.question.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// 작성한 리포지터리를 테스트하기 위해서 JUnit 기반의 스프링부트의 테스트 프레임워크

// testJpa 메서드는 q1, q2 라는 Question 엔티티 객체를 생성하고 QuestionRepository를 이용하여 그 값을 데이터베이스에 저장하는 코드
@SpringBootTest
class SsbApplicationTests {

    @Autowired // 3-02
    private QuestionService questionService;

    @Autowired // questionRepository 객체를 스프링이 자동으로 생성 - DI(의존성 주입)
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;
    // 순환참조 문제와 같은 이유로 @Autowired 보다는 생성자를 통한 객체 주입방식이 권장된다
    // Test코드에서는 생성자를 통한 의존성 주입이 불가능해서 테스트 코드 작성 시에만 @Autowired사용하고
    // 실제 코드 작성시에는 생성자를 통한 의존성 주입을 사용할 예정

    // 3-02 더미 데이터를 추가할 때는 @Transactional를 주석 처리 해줘야 함
    //@Transactional // 메서드가 종료될 때까지 DB 세션이 유지
    @Test // testJPA 메서드가 테스트 메서드임을 나타내는 어노테이션
    void testJPA() {

        //== 1. insert TestCode
        /*Question q1 = new Question();
        q1.setSubject("subject 제목1 테스트");
        q1.setContent("content 내용1 테스트");
        q1.setCreateDate(LocalDateTime.now()); // Local 현재 시간
        this.questionRepository.save(q1); // 1번째 질문(Question) 저장

        Question q2 = new Question();
        q2.setSubject("subject 제목2 테스트");
        q2.setContent("content 내용2 테스트");
        q2.setCreateDate(LocalDateTime.now()); // Local 현재 시간
        this.questionRepository.save(q2); // 2번째 질문(Question) 저장*/

        // 2. findAll
        /*List<Question> all = this.questionRepository.findAll(); // findAll은 데이터를 조회할때 사용하는 메서드이다.
        assertEquals(6, all.size()); // assertEquals(기대값, 실제값)와 같이 사용하고 기대값과 실제값이 동일한지를 조사

        Question q = all.get(0);
        assertEquals("subject 제목1 테스트", q.getSubject());*/

        // 3. finById
        //    - Question 엔티티의 id값으로 데이터 조회
        /*Optional<Question> oq = this.questionRepository.findById(1);
        if(oq.isPresent()) {
            Question q = oq.get();
            assertEquals("subject 제목1 테스트", q.getSubject());

            // findById의 리턴 타입은 Question이 아닌 Optional을 씀
            // Optional은 null 처리를 유연하게 처리하기 위해 사용하는 클래스
            // 위와 같이 isPresent로 null이 아닌지를 확인 후 get으로 실제 Question 객체 값을 얻어야 한다.

        }*/

        // 4. findBySubject
        //    - Question 엔티티의 subject 값으로 데이터를 조회
        // 주의 : Question 리포지터리는 findBySubject와 같은 메서드를 기본적으로 제공하지 않음.
        // findBySubject 메서드를 사용하려면 QuestionRepository 인터페이스를 변경해야 함

        /*Question q = this.questionRepository.findBySubject("subject 제목1 테스트");
        assertEquals(1,q.getId());*/

        // 5. findBySubjectAndContent
        //    - 제목 + 내용 함께 조회(Select) / 두 개의 속성을 And 조건으로 조회할때는 리포지터리에 다음과 같은 메서드를 추가해야 한다.
        /*Question q = this.questionRepository.findBySubjectAndContent(
                "subject 제목1 테스트", "content 내용1 테스트");
        assertEquals(1,q.getId());*/

        // 6. findBySubjectLike
        //    - 제목에 특정 문자열이 포함되어 있는 데이터를 조회 (QuestionRepository 수정)
        /*List<Question> qList = this.questionRepository.findBySubjectLike("sub%");
        Question q = qList.get(0);
        assertEquals("subject 제목1 테스트", q.getSubject());*/

        // 7. 데이터 수정(Update)
        /*Optional<Question> oq = this.questionRepository.findById(1);
        assertTrue(oq.isPresent()); // assertTrue(값) : 값이 true인지 아닌지 Test
        Question q = oq.get();
        q.setSubject("수정된 제목");
        this.questionRepository.save(q);*/

        // 8. 데이터 삭제(Delete) - 1번째 질문 삭제할 것이다.
        /*assertEquals(2, this.questionRepository.count()); // count() : 해당 repository의 총 데이터 건수를 리턴
        Optional<Question> oq = this.questionRepository.findById(1);
        assertTrue(oq.isPresent());
        Question q = oq.get();
        this.questionRepository.delete(q);
        assertEquals(1, this.questionRepository.count());*/

        // 9. 답변 데이터 생성 후 저장
        /*Optional<Question> oq = this.questionRepository.findById(2);
        assertTrue(oq.isPresent());
        Question q = oq.get();

        Answer a = new Answer();
        a.setContent("네 자동으로 생성됩니다.");
        a.setQuestion(q); // 어떤 질문의 답변인지 알기 위해서 Question 객체가 필요함
        a.setCreateDate(LocalDateTime.now());
        this.answerRepository.save(a);*/
        // 답변 데이터를 생성하려면 질문 데이터가 필요하므로 우선 질문 데이터를 구해야 한다.
        // id가 2인 질문 데이터를 가져온 다음 Answer 엔티티의 question 속성에 방금 가져온 질문 데이터를 대입해(a.setQuestion(q))
        // 답변 데이터를 생성했다. Answer 엔티티에는 어떤 질문에 해당하는 답변인지 연결할 목적으로 question 속성이 필요하다.

        // 10. 답변 조회하기
        /*Optional<Answer> oa = this.answerRepository.findById(1); // id 값이 1인 답변을 조회
        assertTrue(oa.isPresent());
        Answer a = oa.get();
        assertEquals(2, a.getQuestion().getId()); // 그 답변의 질문 id가 2인지도 테스트*/

        // 11. 답변에 연결된 질문 찾기 VS 질문에 달린 답변 찾기

        // (1) - 질문으로부터 답변 리스트를 구하는 TestCode
       /* Optional<Question> oq = this.questionRepository.findById(2);
        assertTrue(oq.isPresent());
        Question q = oq.get();

        List<Answer> answerList = q.getAnswerList();

        assertEquals(1, answerList.size());
        assertEquals("네 자동으로 생성됩니다.", answerList.get(0).getContent());*/

        // 3-02  데이터를 생성하는 테스트 케이스(페이징)
        // - 총 300개의 테스트 데이터를 생성한다.
        /*for (int i=1; i<=300; i++) {
            String subject = String.format("테스트 데이터입니다:[%03d]",i);
            String content = "내용무";
            this.questionService.create(subject, content); // 제목, 내용
        }*/

    }

}
