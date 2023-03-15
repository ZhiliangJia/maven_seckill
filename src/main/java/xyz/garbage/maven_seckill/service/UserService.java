package xyz.garbage.maven_seckill.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xyz.garbage.maven_seckill.bean.User;
import xyz.garbage.maven_seckill.exception.GlobalException;
import xyz.garbage.maven_seckill.mapper.UserMapper;
import xyz.garbage.maven_seckill.redis.IRedisKey;
import xyz.garbage.maven_seckill.redis.impl.PrefixEnum;
import xyz.garbage.maven_seckill.redis.impl.RedisKey;
import xyz.garbage.maven_seckill.result.StatusCode;
import xyz.garbage.maven_seckill.util.RedisUtil;
import xyz.garbage.maven_seckill.util.UUIDUtil;
import xyz.garbage.maven_seckill.vo.LoginVo;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    RedisUtil redisUtil;

    private final String COOKIE_NAME_TOKEN;

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    public UserService(@Value("${cookie.token.name}") String cookieTokenName) {
        this.COOKIE_NAME_TOKEN = cookieTokenName;
    }

    /**
     * 后续防止缓存击穿：
     * 1. 加synchronized关键字
     * 2. 乐观锁实现
     * <p>
     * 增加布隆过滤器，减少恶意访问
     *
     * @param id
     * @return
     */
    public User getById(long id) {
        IRedisKey realKey = RedisKey.createSpecialKey(PrefixEnum.USER_BY_ID, String.valueOf(id));
        User user = redisUtil.getStringValue(realKey, User.class);
        if (user != null) return user;
        user = userMapper.getById(id);
        if (user != null) redisUtil.setStringValue(realKey, user);
        return user;
    }

    public boolean updatePassword(long id, String password) {
        User user = getById(id);
        if (user == null) throw new GlobalException(StatusCode.MOBILE_NOT_EXIST);
        User toBeUpdate = new User();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(password);
        userMapper.updatePassword(toBeUpdate);
        IRedisKey realKey = RedisKey.createSpecialKey(PrefixEnum.USER_BY_ID, String.valueOf(id));
        redisUtil.decr(realKey);
        return true;
    }

    public String login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) throw new GlobalException(StatusCode.SERVER_ERROR);
        User user = getById(Long.parseLong(loginVo.getMobile()));
        if (user == null) throw new GlobalException(StatusCode.MOBILE_NOT_EXIST);
        if (!user.getPassword().equals(loginVo.getPassword())) throw new GlobalException(StatusCode.PASSWORD_ERROR);
        String token = UUIDUtil.uuid();
        addCookie(response, token, user);
        return token;
    }

    public void addCookie(HttpServletResponse response, String token, User user) {
        redisUtil.setStringValue(RedisKey.createSpecialKey(PrefixEnum.USER_TOKEN, token), user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(PrefixEnum.USER_TOKEN.getTime());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public User getByToken(HttpServletResponse response, String token) {
        if (token == null || "".equals(token)) return null;
        User user = redisUtil.getStringValue(RedisKey.createSpecialKey(PrefixEnum.USER_TOKEN, token), User.class);
        if (user != null) addCookie(response, token, user);
        return user;
    }
}
