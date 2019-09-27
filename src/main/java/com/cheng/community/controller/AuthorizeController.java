package com.cheng.community.controller;

import com.cheng.community.dto.AccessTokenDTO;
import com.cheng.community.dto.GithubUser;
import com.cheng.community.mapper.UserMapper;
import com.cheng.community.model.User;
import com.cheng.community.provider.GithubProvider;
import com.cheng.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletResponse response){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        System.out.println("accessToken:" + accessToken);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if(githubUser != null && githubUser.getId() != null){
            System.out.println(githubUser.getLogin());
            System.out.println("avatar:" + githubUser.getAvatar_url());
            //登录成功，将用户信息存入数据库
            User user = new User();
            //生成一个token
            String token = UUID.randomUUID().toString();
            System.out.println("token:"+ token);
            user.setToken(token);
            user.setName(githubUser.getLogin());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setAvatarUrl(githubUser.getAvatar_url());

            userService.createOrUpdate(user);
            response.addCookie(new Cookie("token", token));
            return "redirect:/";
        }else {
            System.out.println("githubUser = null");
            //登录失败，重新登陆
            return "redirect:/";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response){
        // 移除session
        request.getSession().removeAttribute("user");
        // 删除cookie
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
