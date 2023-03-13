package xyz.garbage.maven_seckill.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value = "/good")
public class GoodController {

    @RequestMapping(value = "to_list", method = RequestMethod.GET)
    public ModelAndView toList() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("goods_list");
        return modelAndView;
    }
}
