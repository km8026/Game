package com.project.liar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
  @GetMapping("/index")
  public String index() {
    return "html/index";
  }
  @GetMapping("/index2")
  public String index2() {
    return "html/index2";
  }

  @GetMapping("/roulette")
  public String roulette() {
    return "html/roulette";
  }
  @GetMapping("/tetris")
  public String tetris() {
    return "html/tetris";
  }
  


  // 로그아웃 처리
  @GetMapping("/logout")
  public String logout() {
    // 로그아웃 로직 추가 (예: 세션 삭제, 인증 해제 등)
    // Spring Security에서는 /logout 경로로 POST 요청을 보내면 자동 로그아웃 처리됨
    return "redirect:/login"; // 로그아웃 후 로그인 페이지로 리다이렉트, logout 파라미터 추가
  }
}
