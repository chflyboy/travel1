package cn.itcast.travel.web.servlet;

import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.service.impl.CategoryServiceImpl;
import cn.itcast.travel.web.baseservlet.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/Category/*")
public class CategoryServlet extends BaseServlet {
    private CategoryService service = new CategoryServiceImpl();

    /**
     * 设置header.html中的导航列表
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void getNav(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到NavServlet..");
        //调用CategoryServiceImpl对象的方法,获取导航类目
        String list = service.findNav();
        //设置响应字符集编码
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(list);
    }

}
