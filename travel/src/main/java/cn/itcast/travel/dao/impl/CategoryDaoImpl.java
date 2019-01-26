package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class CategoryDaoImpl implements CategoryDao {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());
    /**
     * 查询导航
     * @return
     */
    @Override
    public List<Category> findNav() {
        try {
            //定义SQL
            String sql = "select * from tab_category order by cid";
            //执行SQL
            List<Category> categories = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Category>(Category.class));
            return categories;
        } catch (DataAccessException e) {
            return null ;
        }
    }
}
