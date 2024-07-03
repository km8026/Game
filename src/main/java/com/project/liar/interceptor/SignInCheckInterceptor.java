package com.project.liar.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.project.liar.entity.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component

public class SignInCheckInterceptor implements HandlerInterceptor {
  @Override
  public boolean preHandle(
      HttpServletRequest request, HttpServletResponse response,
      Object handler) throws Exception {
    
        // 현재 요청의 세션을 가져옵니다. 세션이 없다면 새로 생성합니다.
    HttpSession session = request.getSession();

    // 세션에서 'user_info' 속성으로 저장된 User 객체를 가져옵니다.
    User user = (User) session.getAttribute("user_info");

    // 만약 'user_info'가 없다면 (즉, 사용자가 로그인하지 않은 상태라면)
    if (user == null) {
      // 로그인 페이지로 리다이렉트합니다.
      response.sendRedirect("/login");

      // false를 반환하여 요청 처리를 중단합니다.
      return false;
    }

    // 사용자가 로그인한 상태라면 요청 처리를 계속 진행합니다.
    return true;
  }
}
