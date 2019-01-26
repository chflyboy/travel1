package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

public class UserServiceImpl implements UserService {
    private UserDao dao = new UserDaoImpl();

    /**
     *  用户注册
     *  如果可以注册,则将用户输入的信息储存至数据库中,并发送激活邮件
     * @param loginUser
     * @return
     */
    @Override
    public boolean registerUser(User loginUser) {
        String username = loginUser.getUsername();
        System.out.println("从UserServiceImpl中获得的username为:"+username);
        // 对用户在页面输入的用户名进行校验,判断是否在数据库中存在,如果已存在,则无法进行注册
        //调用UserDao中的findOne()方法进行校验
        User user = dao.findOne(username);
        System.out.println("调用数据库中查出的user:"+user);
        if (user == null) {
            System.out.println("向数据库中储存用户");
            //如果该用户名不存在,则可以进行注册
            //设置账户为未激活状态和分配激活码
            loginUser.setStatus("N");//状态
            loginUser.setCode(UuidUtil.getUuid());//分配激活码
            //调用saveUser()方法,将用户的注册信息储存在数据库
            dao.saveUser(loginUser);
            System.out.println("数据库完成储存,发生激活邮件");
            //用户的信息储存后,对用户发送激活邮件,方便用户激活账号

            String contant = "<a href='http://localhost/travel/ActivateServlet?code="+loginUser.getCode()+"'>点击激活【黑马旅游网】</a>";
            MailUtils.sendMail(loginUser.getEmail(),contant,"激活邮件");
            //将用户的注册成功状态返回(true)
            return true;
        }else {
            //如果用户名已存在.返回注册失败的状态(false)
            return false;
        }
    }

    /**
     * 用户登录操作
     * @param loginuser
     * @return
     */
    @Override
    public User findUser(User loginuser) {
        String username = loginuser.getUsername();
        String password = loginuser.getPassword();
        User user = dao.FindByUsernameAndPassword(username, password);
        return user;
    }

    /**
     * 账户激活操作
     * @param code
     */
    @Override
    public boolean activate(String code) {
        User user = dao.isExistByCode(code);
        if (user != null){
            dao.activate(code);
            return true;
        }else {
            return false;
        }
    }
}
