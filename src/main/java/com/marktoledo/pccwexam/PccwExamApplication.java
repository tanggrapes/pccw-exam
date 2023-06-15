package com.marktoledo.pccwexam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;


@SpringBootApplication
public class PccwExamApplication {

    public static void main(String[] args) {
        SpringApplication.run(PccwExamApplication.class, args);
    }

    @Controller
    @RequestMapping("/")
    public class RedirectController {

        @GetMapping("/users")
        public RedirectView redirectWithUsingRedirectView(
                RedirectAttributes attributes) {
            return new RedirectView("/");
        }
    }



}
