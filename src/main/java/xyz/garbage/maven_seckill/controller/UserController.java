package xyz.garbage.maven_seckill.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    @ResponseBody
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public String getUserInfo() {
        return "Success";
    }

}
