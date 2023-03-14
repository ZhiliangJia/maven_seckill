package xyz.garbage.maven_seckill.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.garbage.maven_seckill.vo.GoodsVo;

import java.util.List;

@SpringBootTest
public class GoodsServiceTest {

    @Autowired
    private GoodsService goodsService;

    @Test
    public void demo01() {
        List<GoodsVo> goodsVoList = goodsService.getGoodsVoList();
        for (GoodsVo goodsVo : goodsVoList) {
            System.out.println(goodsVo);
        }
    }
}
