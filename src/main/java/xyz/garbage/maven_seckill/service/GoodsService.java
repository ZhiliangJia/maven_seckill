package xyz.garbage.maven_seckill.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xyz.garbage.maven_seckill.bean.SeckillGoods;
import xyz.garbage.maven_seckill.exception.GlobalException;
import xyz.garbage.maven_seckill.mapper.GoodsMapper;
import xyz.garbage.maven_seckill.redis.impl.PrefixEnum;
import xyz.garbage.maven_seckill.redis.impl.RedisKey;
import xyz.garbage.maven_seckill.result.StatusCode;
import xyz.garbage.maven_seckill.util.RedisUtil;
import xyz.garbage.maven_seckill.vo.GoodsVo;

import java.util.List;

@Service
public class GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private RedisUtil redisUtil;

    private final Integer DEFAULT_MAX_RETRIES;

    public GoodsService(@Value("${optimistic_lock.goods.stock}") Integer defaultMaxRetries) {
        this.DEFAULT_MAX_RETRIES = defaultMaxRetries;
    }

    public List<GoodsVo> getGoodsVoList() {
        return goodsMapper.getGoodsVoList();
    }

    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        GoodsVo cache = redisUtil.getStringValue(RedisKey.createSpecialKey(PrefixEnum.GOOD_DETAIL, String.valueOf(goodsId)), GoodsVo.class);
        if (cache != null) return cache;
        GoodsVo goodsVo = goodsMapper.getGoodsVoByGoodsId(goodsId);
        if (goodsVo != null)
            redisUtil.setStringValue(RedisKey.createSpecialKey(PrefixEnum.GOOD_DETAIL, String.valueOf(goodsId)), goodsVo);
        return goodsVo;
    }

    /**
     * 将秒杀数据写入到Redis中，完成Mysql与Redis中的一致性
     *
     * @param goodsVo
     * @return
     */
    public boolean reduceStock(GoodsVo goodsVo) {
        SeckillGoods seckillGoods = new SeckillGoods();
        seckillGoods.setGoodsId(goodsVo.getId());
        seckillGoods.setVersion(goodsVo.getVersion());
        int answer = 0;
        for (int time = 0; time < DEFAULT_MAX_RETRIES; time++) {
            try {
                seckillGoods.setVersion(goodsMapper.getVersionByGoodsId(goodsVo.getId()));
                answer = goodsMapper.reduceStockByVersion(seckillGoods);
            } catch (Exception e) {
                throw new GlobalException(StatusCode.SERVER_ERROR);
            }
            if (answer != 0) break;
        }
        return answer > 0;
    }

}
