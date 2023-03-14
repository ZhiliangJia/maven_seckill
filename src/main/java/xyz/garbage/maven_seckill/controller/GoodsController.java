package xyz.garbage.maven_seckill.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import xyz.garbage.maven_seckill.bean.User;
import xyz.garbage.maven_seckill.redis.impl.PrefixEnum;
import xyz.garbage.maven_seckill.redis.impl.RedisKey;
import xyz.garbage.maven_seckill.result.Result;
import xyz.garbage.maven_seckill.service.GoodsService;
import xyz.garbage.maven_seckill.service.UserService;
import xyz.garbage.maven_seckill.util.RedisUtil;
import xyz.garbage.maven_seckill.vo.GoodsDetailVo;
import xyz.garbage.maven_seckill.vo.GoodsVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Locale;

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

    @RequestMapping(value = "/to_list", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String toList(HttpServletRequest request, HttpServletResponse response, Locale locale, Model model, User user) {
        String cache = redisUtil.getStringValue(RedisKey.createSpecialKey(PrefixEnum.GOOD_LIST, ""), String.class);
        if (cache != null && !"".equals(cache)) return cache;

        List<GoodsVo> goodsVoList = goodsService.getGoodsVoList();
        model.addAttribute("goodsList", goodsVoList);

        WebContext context = new WebContext(request, response, request.getServletContext(), locale, model.asMap());
        String goodsListHtml = thymeleafViewResolver.getTemplateEngine().process("goods_list", context);
        if (goodsListHtml != null && !"".equals(goodsListHtml))
            redisUtil.setStringValue(RedisKey.createSpecialKey(PrefixEnum.GOOD_LIST, ""), goodsListHtml);

        return goodsListHtml;
    }

    @RequestMapping(value = "/detail/{goodsId}", method = RequestMethod.GET)
    @ResponseBody
    public Result<GoodsDetailVo> detail(HttpServletRequest request, HttpServletResponse response, Model model,
                                        User user, @PathVariable("goodsId") long goodsId) {
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goodsVo);

        long startTime = goodsVo.getStartDate().getTime();
        long endTime = goodsVo.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int seckillStatus = 0, remainSeconds = 0;
        if (now < startTime) {
            seckillStatus = 0;
            remainSeconds = (int) ((startTime - now) / 1000);
        } else if (now > endTime) {
            seckillStatus = 2;
            remainSeconds = -1;
        } else {
            seckillStatus = 1;
            remainSeconds = 0;
        }
        GoodsDetailVo vo = new GoodsDetailVo();
        vo.setGoods(goodsVo);
        vo.setUser(user);
        vo.setRemainSeconds(remainSeconds);
        vo.setSeckillStatus(seckillStatus);
        return Result.success(vo);
    }
}
