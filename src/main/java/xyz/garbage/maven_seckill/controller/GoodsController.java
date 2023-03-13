package xyz.garbage.maven_seckill.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import xyz.garbage.maven_seckill.service.GoodsService;
import xyz.garbage.maven_seckill.service.UserService;
import xyz.garbage.maven_seckill.util.RedisUtil;

@Controller
@RequestMapping(value = "/goods")
public class GoodsController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @RequestMapping(value = "to_list", method = RequestMethod.GET, produces = "text/html")
    public String toList() {
        return "<b>Hello</b>";
    }
}
