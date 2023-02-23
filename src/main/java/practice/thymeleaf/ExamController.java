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

    @Data
    static class examData {
        private String name;
        private int num;

        public examData(String name, int num) {
            this.name = name;
            this.num = num;
        }
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
}
