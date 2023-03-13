package xyz.garbage.maven_seckill.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import xyz.garbage.maven_seckill.bean.User;

@Mapper
public interface UserMapper {
    @Select("select * from sk_user where id = #{id}")
    public User getById(@Param("id") long id);

    @Update("update set password = #{password} where id = #{id}")
    public Integer updatePassword(User password);
}
