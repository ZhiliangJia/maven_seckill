package xyz.garbage.maven_seckill.unit;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class DruidTest {

    private final Logger log = LoggerFactory.getLogger(DruidTest.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DataSource dataSource;

    @Test
    public void demo01() {
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select * from sk_user");
        for (Map<String, Object> map : maps) {
            System.out.println(map);
        }
        log.info("当前数据源：{}", dataSource.getClass().getName());
    }
}
