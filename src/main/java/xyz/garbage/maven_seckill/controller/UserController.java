package xyz.garbage.maven_seckill.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public String getUserInfo() {
        return "Success";
    }

}
