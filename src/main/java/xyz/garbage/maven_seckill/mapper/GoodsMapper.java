package xyz.garbage.maven_seckill.mapper;

import org.apache.ibatis.annotations.*;
import xyz.garbage.maven_seckill.bean.SeckillGoods;
import xyz.garbage.maven_seckill.vo.GoodsVo;

import java.util.List;

@Mapper
public interface GoodsMapper {
    @Results(id = "GoodsAndSeckillResult", value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "goodsName", column = "goods_name"),
            @Result(property = "goodsTitle", column = "goods_title"),
            @Result(property = "goodsImg", column = "goods_img"),
            @Result(property = "goodsDetail", column = "goods_detail"),
            @Result(property = "goodsPrice", column = "goods_price"),
            @Result(property = "goodsStock", column = "goods_stock"),
            @Result(property = "stockCount", column = "stock_count"),
            @Result(property = "startDate", column = "start_date"),
            @Result(property = "endDate", column = "end_date"),
            @Result(property = "seckillPrice", column = "seckill_price"),
            @Result(property = "version", column = "version")
    })
    @Select("select g.*, sg.stock_count, sg.start_date, sg.end_date, sg.seckill_price, sg.version " +
            "from sk_goods_seckill sg left join sk_goods g on sg.goods_id = g.id")
    public List<GoodsVo> getGoodsVoList();

    @ResultMap("GoodsAndSeckillResult")
    @Select("select g.*, sg.stock_count, sg.start_date, sg.end_date, sg.seckill_price, sg.version  from sk_goods_seckill sg left join sk_goods g  on sg.goods_id = g.id where g.id = #{goodsId}")
    public GoodsVo getGoodsVoByGoodsId(@Param("goodsId") long goodsId);

    @Update("update sk_goods_seckill set stock_count = stock_count - 1, version= version + 1 where goods_id = #{goodsId} and stock_count > 0" +
            " and version = #{version}")
    public int reduceStockByVersion(SeckillGoods seckillGoods);

    @Select("select version from sk_goods_seckill  where goods_id = #{goodsId}")
    public int getVersionByGoodsId(@Param("goodsId") long goodsId);

}
