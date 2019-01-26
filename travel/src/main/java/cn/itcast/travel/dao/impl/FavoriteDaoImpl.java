package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FavoriteDaoImpl implements FavoriteDao {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 从数据库中查询,判断用户是否收藏过
     * @param uid
     * @param rid
     * @return
     */
    @Override
    public Favorite isFavorite(int uid, int rid) {
        Favorite favorite = null;
        try {
            String sql = "select * from tab_favorite where uid = ? and rid = ?";
            favorite = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<Favorite>(Favorite.class), uid, rid);
        } catch (DataAccessException e) {
        }
        return favorite;
    }

    /**
     *
     * @param uid
     * @param rid
     */
    @Override
    public void saveFavorite(int uid, int rid) {
        Date date = new Date();
        String sql = "insert into tab_favorite values(?,?,?)";
        int update = jdbcTemplate.update(sql, rid, format.format(date), uid);
    }

    @Override
    public int count(int rid) {
        String sql = "select count(*) from tab_favorite where rid = ?";
        List<Favorite> favorites = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Favorite>(Favorite.class), rid);
        return favorites.size();
    }

    @Override
    public List<Favorite> findAll(int uid) {
        String sql = "select # from tab_favorite where uid = ?";
        List<Favorite> favorites = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Favorite>(Favorite.class), uid);
        return favorites;
    }

}
