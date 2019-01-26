package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteimgDao;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class RouteimgDaoImpl implements RouteimgDao {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());
    @Override
    public List<RouteImg> getImgs(int rid) {
        try {
            String sql = "select * from tab_route_img where rid = ?";
            List<RouteImg> imgs = jdbcTemplate.query(sql, new BeanPropertyRowMapper<RouteImg>(RouteImg.class), rid);
            return imgs;
        } catch (DataAccessException e) {
            return null;
        }
    }
}
