package cn.itcast.travel.web.servlet.old;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/RegisterUserServlet")
public class RegisterUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

//        创建返回信息的封装对象
        ResultInfo info = new ResultInfo();

//        校验用户验证码是否输入正确
        if (checkcode_server == null || !checkcode_server.equalsIgnoreCase(yanzhengma)) {
            System.out.println("验证码错误");
//            如果验证码输入错误,则阻止表单提交,并对返回的信息进行封装
            info.setFlag(false);
            info.setErrorMsg("验证码输入错误");
            // 将响应结果序列化为json
            ObjectMapper mapper = new ObjectMapper();
            String s = mapper.writeValueAsString(info);
            //将响应结果回写给客户端
            response.setContentType("application/json;charset=utf-8");
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
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //调用UserServiceImpl中的方法完成用户的注册
        UserService service = new UserServiceImpl();
        boolean b = service.registerUser(loginUser);
        System.out.println(b);


        //用boolean值得b来表示用户注册是否成功
        if (b) {
            //用户名不存在,注册成功
            info.setFlag(true);

            ObjectMapper mapper = new ObjectMapper();
            String s = mapper.writeValueAsString(info);
            //将响应结果回写给客户端
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(s);
        } else {
            //用户名已存在,注册失败
            info.setFlag(false);
            info.setErrorMsg("该用户名已被占用");
            // 将响应结果序列化为json
            ObjectMapper mapper = new ObjectMapper();
            String s = mapper.writeValueAsString(info);
            //将响应结果回写给客户端
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(s);
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
