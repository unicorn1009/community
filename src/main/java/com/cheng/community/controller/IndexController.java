package com.cheng.community.controller;

import com.cheng.community.dto.PaginationDTO;
import com.cheng.community.dto.QuestionDTO;
import com.cheng.community.mapper.QuestionMapper;
import com.cheng.community.mapper.UserMapper;
import com.cheng.community.model.Question;
import com.cheng.community.model.User;
import com.cheng.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(
            HttpServletRequest request,
            Model model,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    User user = userMapper.findByToken(token);
                    if (user != null) {
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }

        PaginationDTO pagination = questionService.list(page,size);
        model.addAttribute("pagination", pagination);

        return "index";
    }
}
