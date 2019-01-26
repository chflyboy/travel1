package cn.itcast.travel.web.servlet.old;

import cn.itcast.travel.dao.impl.UserDaoImpl;
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

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        ResultInfo info = new ResultInfo();//错误信息对象
//        校验验证码是否正确
        if (!checkcode_server.equalsIgnoreCase(check)){
            //验证码输入错误

            //储存错误信息
            info.setFlag(false);
            info.setErrorMsg("验证码输入错误");
            ObjectMapper mapper = new ObjectMapper();
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
        UserService service = new UserServiceImpl();
        User user = service.findUser(loginuser);
        System.out.println("用数据库中查到的user对象"+user.getName());
        if (user == null){
            //用户不存在
            info.setFlag(false);
            info.setErrorMsg("用户名或密码错误");
            ObjectMapper mapper = new ObjectMapper();
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
                ObjectMapper mapper = new ObjectMapper();
                String s = mapper.writeValueAsString(info);
                response.getWriter().write(s);
            }else {
                //用户未激活
                info.setFlag(false);
                info.setErrorMsg("该账号未激活,请激活");
                ObjectMapper mapper = new ObjectMapper();
                String s = mapper.writeValueAsString(info);
                response.getWriter().write(s);
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
