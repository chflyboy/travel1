package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class RouteDaoImpl implements RouteDao {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * 查询分组
     *
     * @param cid
     * @return
     */
    @Override
    public List<Route> groupByCid(int cid) {
        //定义SQL
        String sql = "select * from tab_route where cid = ?";
        //执行sql
        List<Route> routes = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Route>(Route.class), cid);
        return routes;
    }

    @Override
    public List<Route> groupByCid(int cid, int start, int countForPage) {
        String sql = "select * from tab_route where cid = ? limit ?,?";
        List<Route> routes = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Route>(Route.class), cid, start, countForPage);
        return routes;
    }

    /**
     * 模糊查询,获取所有相关数据
     *
     * @param rname
     * @return
     */
    @Override
    public List<Route> search_All(int cid, String rname) {
        System.out.println("Dao层----cid:"+cid);
        System.out.println("Dao层----rname"+rname);
        try {
            List list = new ArrayList<>();
            String sql = "select * from tab_route where 1 = 1";

            StringBuilder builder = new StringBuilder(sql);
            if (cid != 0) {
                builder.append(" and cid = ?");
                list.add(cid);
            }
            if (rname != null && rname.length() > 0 && !"null".equals(rname)) {
                rname = "%" + rname + "%";
                builder.append(" and rname like ?");
                list.add(rname);
            }
            sql = builder.toString();
            List<Route> search_list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Route>(Route.class), list.toArray());
            System.out.println("离开Dao");
            return search_list;
        } catch (DataAccessException e) {
            return null;
        }

    }

    /**
     * 模糊查询
     * 分页查询
     * @param cid
     * @param start
     * @param countForPage
     * @param rname
     * @return
     */
    @Override
    public List<Route> search_list(int cid, int start, int countForPage, String rname) {
        System.out.println("进入Dao----开始分页");
        List list = new ArrayList<>();
        String sql = "select * from tab_route where 1 = 1 ";
        StringBuilder builder = new StringBuilder(sql);
        if (cid != 0) {
            builder.append(" and cid = ? ");
            list.add(cid);
        }
        if (rname != null && rname.length() > 0 && !"null".equals(rname) ){
            rname = "%" + rname + "%";
            builder.append(" and rname like ? ");
            list.add(rname);
        }

        builder.append(" limit ? ,? ");

        list.add(start);
        list.add(countForPage);
        sql= builder.toString();
        System.out.println("分页sql为:"+sql);
        System.out.println(list.toArray());
        List<Route> routeList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Route>(Route.class), list.toArray());
        return routeList;
    }

    @Override
    public Route getRoute(int rid) {
        String sql = "select * from tab_route where rid = ?";
        return jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<Route>(Route.class),rid);
    }
}
