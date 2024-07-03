package com.project.liar.controller;

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
    UserRepository userRepository;
    @Autowired
	HttpSession session;

    @GetMapping("/checkUsername")
    @ResponseBody
    public boolean checkUsername(@RequestParam String username) {
        return userService.isUsernameTaken(username);
    }

    @GetMapping("/checkNickname")
    @ResponseBody
    public boolean checkNickname(@RequestParam String nickname) {
        return userService.isNicknameTaken(nickname);
    }

    @PostMapping("/signup")
    public String signupUser(@ModelAttribute User user, Model model) {
        try {
            User newUser = userService.signupUser(user.getNickname(), user.getUsername(), user.getPassword());
            model.addAttribute("user", newUser);
            return "redirect:/login"; // 회원가입 후 로그인 페이지로 리다이렉트
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
            return "redirect:/index"; // 로그인 후 홈 페이지로 리다이렉트
        } else {
            return "redirect:/users/login?error=true"; // 로그인 실패 시 로그인 페이지로 리다이렉트
        }
    }

    @PostMapping("/login2")
	public String loginPost(@ModelAttribute User user) {
		User dbUser = 
			userRepository.findByUsernameAndPassword(
				user.getUsername(), user.getPassword());
		if(dbUser != null) {
			session.setAttribute("user_info", dbUser);
		}
		return "redirect:/index";
	}
}