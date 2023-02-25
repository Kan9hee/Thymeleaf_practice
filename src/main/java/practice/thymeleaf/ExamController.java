package practice.thymeleaf;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/exam")
public class ExamController {

    @GetMapping("/text")
    public String textFunction(Model model) {
        model.addAttribute("data", "Test String");
        return "view/textExam";
    }
    //text="${data}를 통해 textFunction의 모델을 문자열로 출력.
    //[[${data}]]를 통해 태그 내부에 삽입 가능.

    @GetMapping("/unescapedText")
    public String unescapedFunction(Model model) {
        model.addAttribute("data", "<b>Test String</b>");
        return "view/unescapedExam";
    }
    //반환한 모델에 포함된 html구문을 escape/unescape 문법으로 인식을 구분한다.

    @GetMapping("/variable")
    public String variableFunction(Model model) {
        examData d1 = new examData("AAA", 123);
        examData d2 = new examData("BBB", 456);
        examData d3 = new examData("CCC", 789);

        List<examData> list = new ArrayList<>();
        list.add(d1);
        list.add(d2);
        list.add(d3);

        Map<String, examData> map = new HashMap<>();
        map.put("d1", d1);
        map.put("d2", d2);
        map.put("d3", d3);

        model.addAttribute("examData", d1);
        model.addAttribute("list", list);
        model.addAttribute("map", map);

        return "view/variable";
    }
    // (접근할 모델).get변수명() 을 통해 프로퍼티 접근
    // 위와 동일한 방식들:
    //    (접근할 모델)['변수명']
    //    (접근할 모델).변수명
    // 타임리프의 with구문을 통해 태그 내에서만 쓰이는 지역변수 선언 가능

    @GetMapping("/object")
    public String objectFunction(Model model, HttpServletRequest request,
                                 HttpServletResponse response, HttpSession session) {
        session.setAttribute("sessionData", "session");
        model.addAttribute("request", request);
        model.addAttribute("response", response);
        model.addAttribute("servletContext", request.getServletContext());
        return "view/object";
    }

    @Component("examBean")
    static class examBean {
        public String exam(String data) {
            return data;
        }
    }
    // request, response, session, servletContext, locale 제공한다.
    // ${객체명}으로 사용한다.
    // 지역 정보는 ${#locale}로 사용한다.
    // 나아가 요청 파라미터나 세션, 빈에 접근하기 용이하도록 별도로 객체를 제공해준다.
    //    요청 파라미터: param (예: ${param.paramData})
    //    세션: session (예: ${session.sessionData})
    //    빈: @ (예: @examBean.exam('examString')})

    @GetMapping("/date")
    public String dateFunction(Model model) {
        model.addAttribute("localDateTime", LocalDateTime.now());
        return "view/date";
    }
    // 자바의 날짜 객체인 localDateTime을 통해 날짜를 출력할 수도 있고,
    // 타임리프의 #temporals를 통해 세부시간을 알 수 있는 객체를 이용할 수도 있다.

    @GetMapping("/URL-link")
    public String linkFunction(Model model) {
        model.addAttribute("data1", "exam1");
        model.addAttribute("data2", "exam2");
        return "view/URL";
    }
    // href를 이용해 현재 URL에 경로를 추가할 수 있다.
    //    @{/exam}: 기본 방식
    //    @{/exam(쿼리 파라미터=${데이터})}: 쿼리 파라미터 추가
    //    @{/exam/{경로}(경로=${데이터})}: 경로 동적 수정
    //    @{/exam/{변수1}(변수1=${데이터}, 변수2=${데이터})}:
    //      경로 동적 수정, 남은 변수는 쿼리 파라미터화

    @GetMapping("/literal")
    public String literalFunction(Model model) {
        model.addAttribute("data", "exam");
        return "view/literal";
    }
    // 소스코드에서 고정된 값을 리터럴이라고 한다. (상수에 들어가는 값)
    //    "'리터럴내용'" <-작은따옴표로 감싸 표시하며, 내부에 공백이 없어야 한다.
    //                   ||를 통해 리터럴을 치환시켜 편리하게 사용 가능하다.

    @GetMapping("/operation")
    public String operationFunction(Model model) {
        model.addAttribute("nullData", null);
        model.addAttribute("data", "exam");
        return "view/operation";
    }
    // 간단한 연산부터 비교연산, 조건식을 제공한다.
    // 엘비스 연산자(?:): 객체가 null일 경우 우측에 설정한 값을 반환한다.
    //                  _를 설정값에 넣을 경우 html의 내용을 반환한다.

    @GetMapping("/attribute")
    public String attribute(){
        return "view/attribute";
    }
    // xmlns로 설정한 타임리프 네임스페이스명으로 기존 html태그 속성을 대체 가능하다.
    //      attrappend: 속성값의 뒤에 값을 추가한다. 띄어쓰기에 유의해야 한다.
    //      attrprepend: 속성값의 앞에 값을 추가한다. 띄어쓰기에 유의해야 한다.
    //      classappend: class속성에 능동적으로 값을 추가한다.

    @GetMapping("/iteration")
    public String iteration(Model model){
        addExamData(model);
        return "view/iteration";
    }
    // 타임리프에서는 each를 사용해 반복문으로 사용할 수 있다.
    //      네임스페이스:each="변수명: ${컬렉션}": 컬렉션의 값을 하나씩 꺼내는 것을 반복한다.
    //      네임스페이스:each="변수명,변수명+Stat: ${컬렉션}": 두번째 변수를 통해 반복상태 확인
    //          index : 0부터 시작하는 값
    //          count : 1부터 시작하는 값
    //          size : 전체 사이즈
    //          current : 현재 객체
    //          odd, even : 홀수/짝수 확인( boolean )
    //          first, last :처음/마지막 값 확인( boolean )

    @GetMapping("/condition")
    public String condition(Model model) {
        addExamData(model);
        return "view/condition";
    }
    // 기존 자바와 비슷한 조건문이다.
    //      if,unless=${조건}
    //      switch=${배열}, case="조건"

    @GetMapping("/block")
    public String block(Model model){
        addExamData(model);
        return "view/block";
    }
    // 타임리프 고유 태그로, 원하는 속성을 지정할 수 있는 속성 컨테이너다.
    // 각 요소에 대해 하나 이상의 <tr> 이 필요한 반복 테이블 사용에 유용하다.

    @Data
    static class examData {
        private String name;
        private int num;

        public examData(String name, int num) {
            this.name = name;
            this.num = num;
        }
    }

    private void addExamData(Model model){
        List<examData>list=new ArrayList<>();
        list.add(new examData("data1",123));
        list.add(new examData("data2",456));
        list.add(new examData("data3",789));
        model.addAttribute("dataList",list);
    }
}
