package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Category;
import cn.itcast.travel.domain.User;

import java.util.List;

public interface UserDao {
    /**
     * 根据用户名在数据库中查询该用户是否存在
     * @param username
     * @return
     */
    User findOne(String username);

    /**
     * 储存用户信息
     * @param user
     */
    void saveUser(User user);

    /**
     * 登录时,通过用户名和密码查询数据库判断用户是否存在
     * @param username
     * @param password
     * @return
     */
    User FindByUsernameAndPassword(String username,String password);

    /**
     * 校验激活码是否有效
     * @param code
     * @return
     */
    User isExistByCode(String code);

    /**
     * 激活账户
     * @param code
     */
    void activate(String code);



}
