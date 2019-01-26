package cn.itcast.travel.web.servlet.old;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/GetNameServlet")
public class GetNameServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入GetNameServlet中...");
        ResultInfo info = new ResultInfo();
        response.setContentType("application/json;charset=utf-8");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        System.out.println("从session域中取出的用户姓名: " + user);
        ObjectMapper mapper = new ObjectMapper();
        if (user != null) {
            info.setFlag(true);
            info.setData(user);
        } else {
            info.setFlag(false);
        }
        String s = mapper.writeValueAsString(info);
        response.getWriter().write(s);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
