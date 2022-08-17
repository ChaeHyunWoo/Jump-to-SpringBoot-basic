package com.mysite.ssb.question;

import com.mysite.ssb.anwer.AnswerForm;
import com.mysite.ssb.user.SiteUser;
import com.mysite.ssb.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

// @중요@   Controller -> Service -> Repository 순으로 데이터 처리


@RequestMapping("/question")
//롬복이 제공하는 애너테이션으로 final이 붙은 속성을 포함하는 생성자를 자동으로 생성하는 역할
@RequiredArgsConstructor // questionRepository 속성을 포함하는 생성자를 생성 - 의존성 주입(DI)
@Controller
public class QuestionController {

    //private final QuestionRepository questionRepository;
    // questionService는 생성자 방식(@RequiredArgsConstructor)으로 의존성 주입 - DI
    private final QuestionService questionService;
    private final UserService userService;


    // http://localhost:8080/question/list?page=0 처럼 GET 방식으로 요청된 URL에서 page값을 가져오기 위해
    // list()에 @RequestParam(value="page", defaultValue="0") int page 매개변수 추가
    // + URL에 페이지 파라미터 page가 전달되지 않은 경우 디폴트 값(기본값)으로 0이 되도록 설정 / JPA 페이징은 번호가 0부터 시작!!!
    @RequestMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {

        Page<Question> paging = this.questionService.getList(page);
        model.addAttribute("paging", paging);

        return "question_list";
    }

    @RequestMapping("/")
    public String root() {
        return "redirect:/question/list";
        // redirect:<URL> - URL로 리다이렉트 (리다이렉트는 완전히 새로운 URL로 요청이 된다.)
        // forward:<URL> - URL로 포워드 (포워드는 기존 요청 값들이 유지된 상태로 URL이 전환된다.)
    }

    // 질문(Question) 상세 페이지
    //@RequestMapping(value = "/question/detail/{id}")
    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm an) {
        // 요청 URL http://localhost:8080/question/detail/2의 숫자 2처럼 변하는
        // id 값을 얻을 때에는 위와 같이 @PathVariable 애너테이션을 사용해야 한다.
        // 이 때 @RequestMapping(value = "/question/detail/{id}") 에서 사용한 id와 @PathVariable("id")의 매개변수 이름이 동일해야 한다.
        Question question = this.questionService.getQuestion(id);
        model.addAttribute("question", question);

        return "question_detail";
    }

    // 질문 등록창 출력
    @PreAuthorize("isAuthenticated()") // 어노테이션이 붙은 메서드는 로그인이 필요한 메서드를 의미-> 만약 어노티에션이 적용된 메서드가 로그아웃 상태에서 호출되면 로그인 페이지로 이동
    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm) {

        // questionCreate 메서드는 question_form 템플릿을 렌더링하여 출력
        return "question_form";
    }

    // 질문 등록처리
    // - questionCreate 메서드의 매개변수를 subject,content 대신 QuestionForm 객체로 변경
    // - subject,content 항목을 지닌 Form이 전송되면 QuestionForm의 subject,content 속성이 자동으로 바인딩 된다.
    // - 이것이 스프링 프레임워크의 바인딩 기능 + @Valid : QuestionForm의 @NotEmpty, @Size 등으로 설정한 검증 기능이 동작함
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create") // methtod Overroading
    public String questionCreate(@Valid QuestionForm questionForm,
                                 BindingResult bindingResult, Principal principal) {
        // @중요 - BindingResult 매개변수는 항상 @Valid 매개변수 바로 뒤에 위치해야 한다.
        //      - 만약 2개의 매개변수의 위치가 정확하지 않다면 @Valid만 적용이 되어 입력값 검증 실패 시 400 Error

        // 조건문 - 오류가 있을 경우 다시 form을 작성하는 화면 렌더링 -> Error이 없으면 질문 등록 진행
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        // TODO 질문을 저장
        this.questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser);

        return "redirect:/question/list";
    }

    // 질문 수정 - 로그인한 사용자와 작성자가 동일할 경우 수정 가능
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal) {
        Question question = this.questionService.getQuestion(id);
        // 로그인한 사용자와 작성자가 다를 경우 예외 발생
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        // if문 조건을 통과하면 하단 소스 실행 - 일치할 경우
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());

        return "question_form";
    }

    // 질문 수정
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult,
                                 Principal principal, @PathVariable("id") Integer id) {

        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        Question question = this.questionService.getQuestion(id);
        // 질문 작성자와 로그인한 사용자가 일치하지않으면 예외발생
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        // 일치하면 하단 소스 실행
        this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent());

        return String.format("redirect:/question/detail/%s", id);
    }

    // 질문 삭제
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);

        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.questionService.delete(question);

        return "redirect:/"; // 질문 삭제 후 질문목록화면으로 돌아갈 수 있도록  루트 페이지로 리다이렉트
    }

    // 질문 추천버튼 클릭 시 호출되는 URL 처리
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String questionVote(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.questionService.vote(question, siteUser);
        return String.format("redirect:/question/detail/%s", id);
    }

}