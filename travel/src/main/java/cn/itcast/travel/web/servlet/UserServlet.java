package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import cn.itcast.travel.web.baseservlet.BaseServlet;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/User/*")
public class UserServlet extends BaseServlet {
    //        创建返回信息的封装对象
    private ResultInfo info = new ResultInfo();
    private UserService service = new UserServiceImpl();
    private ObjectMapper mapper = new ObjectMapper();

    /**
     * 注册用户
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        System.out.println("进入RegisterUserServlet");
        //        从注册页面获取用户输入的验证码
        String yanzhengma = request.getParameter("check");
        System.out.println("从页面用户输入的验证码为:" + yanzhengma);

//        获取系统随机生成的验证码
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        System.out.println("系统随机生成的验证码为:" + checkcode_server);
        //删除在session中储存的验证码,保证验证码的一次性
        session.removeAttribute("CHECKCODE_SERVER");



//        校验用户验证码是否输入正确
        if (checkcode_server == null || !checkcode_server.equalsIgnoreCase(yanzhengma)) {
            System.out.println("验证码错误");
//            如果验证码输入错误,则阻止表单提交,并对返回的信息进行封装
            info.setFlag(false);
            info.setErrorMsg("验证码输入错误");
            // 将响应结果序列化为json
            String s = mapper.writeValueAsString(info);
            //将响应结果回写给客户端
            response.getWriter().write(s);
            return;
        }

//        如果验证码输入正确,则提交表单
        System.out.println("验证码正确");
//        获取用户注册信息
        User loginUser = new User();
        Map<String, String[]> map = request.getParameterMap();
        /*Set<String> keySet = map.keySet();
        for (String key : keySet) {
            System.out.println("从页面获取的用户信息:  "+key+"=="+map.get(key)[0]);
        }*/
        try {
            BeanUtils.populate(loginUser, map);
            System.out.println("从页面获取的数据封装对象后的取值:  "+loginUser.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //调用UserServiceImpl中的方法完成用户的注册
        boolean b = service.registerUser(loginUser);
        System.out.println(b);

        //用boolean值得b来表示用户注册是否成功
        if (b) {
            //用户名不存在,注册成功
            info.setFlag(true);
            String s = mapper.writeValueAsString(info);
            //将响应结果回写给客户端
            response.getWriter().write(s);
        } else {
            //用户名已存在,注册失败
            info.setFlag(false);
            info.setErrorMsg("该用户名已被占用");
            // 将响应结果序列化为json
            String s = mapper.writeValueAsString(info);
            //将响应结果回写给客户端
            response.getWriter().write(s);
        }
    }

    /**
     * 用户登录
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        //        获取用户输入的验证码
        String check = request.getParameter("check");
        System.out.println("用户输入的验证码: "+check);
//        获取系统随机的验证码
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        System.out.println("系统产生的验证码"+checkcode_server);
//        删除session中存储的验证码
        session.removeAttribute("CHECKCODE_SERVER");
//        校验验证码是否正确
        if (!checkcode_server.equalsIgnoreCase(check)){
            //验证码输入错误

            //储存错误信息
            info.setFlag(false);
            info.setErrorMsg("验证码输入错误");
            String s = mapper.writeValueAsString(info);
            response.getWriter().write(s);
            return;
        }

//        获取用户登录信息并封装为user对象
        Map<String, String[]> map = request.getParameterMap();
        User loginuser = new User();
        try {
            BeanUtils.populate(loginuser,map);
            System.out.println("用户输入的用户名"+loginuser.getUsername());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
//        调用UserService中的findUser()方法查询user是否存在
        User user = service.findUser(loginuser);
        System.out.println("用数据库中查到的user对象"+user.getName());
        if (user == null){
            //用户不存在
            info.setFlag(false);
            info.setErrorMsg("用户名或密码错误");
            String s = mapper.writeValueAsString(info);
            response.getWriter().write(s);
        }else {
            //用户存在
            if (user.getStatus().equals("Y")){
                //用户已激活
                //将用户储存到session中
                session.setAttribute("user",user);
                //将回写信息写入客户端
                info.setFlag(true);
                String s = mapper.writeValueAsString(info);
                response.getWriter().write(s);
            }else {
                //用户未激活
                info.setFlag(false);
                info.setErrorMsg("该账号未激活,请激活");
                String s = mapper.writeValueAsString(info);
                response.getWriter().write(s);
            }
        }
    }

    /**
     * 用户注销
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void loginout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.invalidate();

    }

    /**
     * 获取用户登录后的名字
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void getName(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
      //  System.out.println("从session域中取出的用户姓名: " + user.getName());
        if (user != null) {
            info.setFlag(true);
            info.setData(user);
        } else {
            info.setFlag(false);
        }
        String s = mapper.writeValueAsString(info);
        response.getWriter().write(s);
    }

    /**
     * 用户账号激活
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void activate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        if (code != null){
            boolean activate = service.activate(code);
            if (activate){
                response.getWriter().write("恭喜激活成功,请<a href = 'login.html'>登录</a>");
            }else {
                response.getWriter().write("无效激活码,请重新激活");
            }
        }
    }

    /**
     * 判断用户是否登录
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            System.out.println(user.getName());
        }
        writeValue(user,response);
    }
}
