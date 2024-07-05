package com.project.liar.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.liar.entity.User;
import com.project.liar.repository.UserRepository;
import com.project.liar.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HttpSession session;

    @GetMapping("/checkDuplicate")
    @ResponseBody
    public Map<String, Object> checkDuplicate(@RequestParam String field, @RequestParam String value) {
        boolean isDuplicate = false;
        if ("username".equals(field)) {
            isDuplicate = userService.isUsernameTaken(value);
        } else if ("nickname".equals(field)) {
            isDuplicate = userService.isNicknameTaken(value);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("isDuplicate", isDuplicate);
        return map;
    }

    @PostMapping("/signup")
    public String signupUser(@ModelAttribute User user, Model model) {
        try {
            User newUser = userService.signupUser(user.getNickname(), user.getUsername(), user.getPassword());
            model.addAttribute("user", newUser);
            return "redirect:/"; // 회원가입 후 로그인 페이지로 리다이렉트
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "signup"; // 에러 메시지를 포함하여 signup 페이지로 리다이렉트
        }
    }

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup"; // signup.html 템플릿을 반환
    }

    @GetMapping("/login")
    public String showLoginForm(Model model, @RequestParam(name = "error", required = false) String error) {
        model.addAttribute("user", new User());
        if (error != null) {
            model.addAttribute("errorMessage", "아이디나 비밀번호가 틀렸습니다.");
        }
        return "login"; // login.html 템플릿을 반환
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute User user, Model model) {
        User loggedInUser = userService.loginUser(user.getUsername(), user.getPassword());
        if (loggedInUser != null) {
            model.addAttribute("user", loggedInUser);
            session.setAttribute("user_info", loggedInUser);
            return "redirect:/"; // 로그인 후 홈 페이지로 리다이렉트
        } else {
            return "redirect:/users/login?error=true"; // 로그인 실패 시 로그인 페이지로 리다이렉트
        }
    }

    @GetMapping("/mypage")
    public String showMypage(Model model) {
        User loggedInUser = (User) session.getAttribute("user_info");
        if (loggedInUser == null) {
            return "redirect:/users/login"; // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
        }
        model.addAttribute("user", loggedInUser);
        return "html/mypage"; // mypage.html 템플릿을 반환
    }

    @PostMapping("/updateNickname")
    public String updateNickname(@RequestParam("nickname") String nickname, Model model) {
        User loggedInUser = (User) session.getAttribute("user_info");
        if (loggedInUser == null) {
            return "redirect:/users/login"; // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
        }
        if (!userService.isNicknameTaken(nickname)) {
            loggedInUser.setNickname(nickname);
            userRepository.save(loggedInUser);
            model.addAttribute("user", loggedInUser);
            session.setAttribute("user_info", loggedInUser);
            return "redirect:/users/mypage"; // 업데이트 후 마이페이지로 리다이렉트
        } else {
            model.addAttribute("error", "Nickname already taken.");
            return "html/mypage"; // 에러 메시지를 포함하여 mypage 페이지로 리다이렉트
        }
    }

    @PostMapping("/updateUsername")
    public String updateUsername(@RequestParam("username") String username, Model model) {
        User loggedInUser = (User) session.getAttribute("user_info");
        if (loggedInUser == null) {
            return "redirect:/users/login"; // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
        }
        if (!userService.isUsernameTaken(username)) {
            loggedInUser.setUsername(username);
            userRepository.save(loggedInUser);
            model.addAttribute("user", loggedInUser);
            session.setAttribute("user_info", loggedInUser);
            return "redirect:/users/mypage"; // 업데이트 후 마이페이지로 리다이렉트
        } else {
            model.addAttribute("error", "Username already taken.");
            return "html/mypage"; // 에러 메시지를 포함하여 mypage 페이지로 리다이렉트
        }
    }

    @PostMapping("/updatePassword")
    public String updatePassword(@RequestParam("password") String password, Model model) {
        User loggedInUser = (User) session.getAttribute("user_info");
        if (loggedInUser == null) {
            return "redirect:/users/login"; // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
        }
        // 비밀번호 암호화 및 업데이트
        userService.updateUserPassword(loggedInUser, password);
        model.addAttribute("user", loggedInUser);
        session.setAttribute("user_info", loggedInUser);
        return "redirect:/users/mypage"; // 업데이트 후 마이페이지로 리다이렉트
    }

    @PostMapping("/login2")
    public String loginPost(@ModelAttribute User user) {
        User dbUser = userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
        if (dbUser != null) {
            session.setAttribute("user_info", dbUser);
        }
        return "redirect:/index";
    }
}
