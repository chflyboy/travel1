package cn.itcast.travel.web.servlet.old;

import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.service.impl.CategoryServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/NavServlet")
public class NavServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到NavServlet..");
        //调用CategoryServiceImpl对象的方法,获取导航类目
        CategoryService service = new CategoryServiceImpl();
        String list = service.findNav();
        //设置响应字符集编码
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(list);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
