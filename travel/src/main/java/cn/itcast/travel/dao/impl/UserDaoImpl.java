package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class UserDaoImpl implements UserDao {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * 判断该用户名在数据库中是否存在
     *
     * @param username
     * @return
     */
    @Override
    public User findOne(String username) {
        try {
            //设置SQL语句,根据用户名查询数据库
            String sql = "select * from tab_user where username = ?";
            //执行SQL,返回一个User对象
            User user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username);
            //如果能够查到,则将user对象返回
            return user;
        } catch (DataAccessException e) {
            //如果查不到,则返回一个null
            System.out.println("Dao层中查询结果--数据库中没有查到用户信息");
            return null;
        }
    }

    /**
     * 储存用户信息
     *
     * @param user
     */
    @Override
    public void saveUser(User user) {
        //定义SQL语句,用来储存用户的注册信息
        String sql = "insert into tab_user(username,password,name,birthday,sex,telephone,email,status,code) values(?,?,?,?,?,?,?,?,?)";
        //执行SQL
        jdbcTemplate.update(sql, user.getUsername(), user.getPassword(), user.getName(), user.getBirthday(), user.getSex(), user.getTelephone(), user.getEmail(), user.getStatus(), user.getCode());
    }

    /**
     * 登录时,通过用户名和密码查询数据库判断用户是否存在
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public User FindByUsernameAndPassword(String username, String password) {
        User user = null;
        try {
            //定义SQL语句
            String sql = "select * from tab_user where username = ? and password = ?";
            // 执行SQL语句,查询
            user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username, password);
        } catch (DataAccessException e) {
            System.out.println("数据库中未查到该用户");
        }
        return user;
    }

    @Override
    public User isExistByCode(String code) {
        User user = null;
        try {
            //定义SQL
            String sql = "select * from tab_user where code = ?";
            //执行SQL
           user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), code);
        } catch (DataAccessException e) {
            System.out.println("此激活码无效");
        }
        return user;
    }

    /**
     * 激活账户
     *
     * @param code
     */
    @Override
    public void activate(String code) {
        //定义SQL
        String sql = "update tab_user set status = 'Y' where code = ? ";
        //执行sql
        jdbcTemplate.update(sql, code);
    }



}
