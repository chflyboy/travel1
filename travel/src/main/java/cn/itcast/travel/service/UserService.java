package cn.itcast.travel.service;

import cn.itcast.travel.domain.User;

public interface UserService {
    /**
     * 注册:
     * 查询用户是否存在
     * @param loginUser
     * @return
     */
    boolean registerUser(User loginUser);

    /**
     * 登录:
     * 查询用户是否存在
     * @param user
     * @return
     */
    User findUser(User user);

    /**
     * 激活账号
     * @param code
     */
    boolean activate(String code);
}
