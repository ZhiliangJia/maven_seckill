package xyz.garbage.maven_seckill.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.garbage.maven_seckill.result.Result;
import xyz.garbage.maven_seckill.service.UserService;
import xyz.garbage.maven_seckill.vo.LoginVo;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@Controller
@RequestMapping(value = "/login")
public class LoginController {

    private final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/do_login", method = RequestMethod.POST)
    @ResponseBody
    public Result<String> doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {
        return Result.success(userService.login(response, loginVo));
    }

    @RequestMapping(value = "/to_login", method = RequestMethod.GET)
    public String toLogin() {
        return "login";
    }
}
