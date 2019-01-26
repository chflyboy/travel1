package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.Page;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;
import cn.itcast.travel.web.baseservlet.BaseServlet;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/page/*")
public class PageServlet extends BaseServlet {
    private RouteService service = new RouteServiceImpl();
    private FavoriteService favoriteService = new FavoriteServiceImpl();
    private ObjectMapper mapper = new ObjectMapper();

    /**
     * 从前台接收cid,pageNumber,countForPage等信息,
     * 动态获取数据库中的相关信息并返回到前台
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void getPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        //从客户端获取所选类的cid,当前页码,每页条目数量
        String cid_str = request.getParameter("cid");
        String pageNumber_str = request.getParameter("pageNumber");
        String countForPage_str = request.getParameter("countForPage");
        int cid;
        int pageNumber;
        int countForPage;
        //对获取的参数进行校验处理
        if (pageNumber_str == null || pageNumber_str.length() == 0) {
            pageNumber = 1;
        } else {
            pageNumber = Integer.parseInt(pageNumber_str);
        }

        if (cid_str == null || cid_str.length() == 0) {
            cid = 5;
        } else {
            cid = Integer.parseInt(cid_str);
        }

        if (countForPage_str == null || countForPage_str.length() == 0) {
            countForPage = 8;
        } else {
            countForPage = Integer.parseInt(countForPage_str);
        }
        //对获取的参数进行处理(转换为int类型)

        //调用方法,从数据库动态获取当前页的数据内容
        Page page = service.listForPage(cid, pageNumber, countForPage);

        //将从数据库中获取到的数据信息写回到客户端
        String s = mapper.writeValueAsString(page);
        response.getWriter().write(s);
    }

    /**
     * 进行模糊查询
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void searchtravel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入searchtravel...");
        response.setContentType("application/json;charset=utf-8");
        String rname = request.getParameter("rname");
        rname = new String(rname.getBytes("iso-8859-1"), "utf-8");
        System.out.println("rname-----" + rname);
        //从客户端获取所选类的cid,当前页码,每页条目数量
        String cid_str = request.getParameter("cid");
        System.out.println("cid---------:" + cid_str);
        String pageNumber_str = request.getParameter("pageNumber");
        System.out.println("pagename----------" + pageNumber_str);
        String countForPage_str = request.getParameter("countForPage");
        System.out.println("countForPage-------------" + countForPage_str);
        int cid = 0;
        int pageNumber = 1;
        int countForPage = 8;
        //对获取的参数进行校验处理
        if (pageNumber_str != null && pageNumber_str.length() != 0 && !"null".equals(pageNumber_str)) {
            pageNumber = Integer.parseInt(pageNumber_str);
        }

        if (cid_str != null && cid_str.length() != 0 && !"null".equals(cid_str)) {
            cid = Integer.parseInt(cid_str);
        }

        if (countForPage_str != null && countForPage_str.length() != 0 && !"null".equals(countForPage_str)) {
            countForPage = Integer.parseInt(countForPage_str);
        }

        System.out.println("PageServlet中获取到的cid为:" + cid);
        System.out.println("PageServlet中获取到的pageNumber为" + pageNumber);
        System.out.println("PageServlet中获取到的countForPage为" + countForPage);
        System.out.println("PageServlet中获取到的rname为" + rname);

        //调用方法,从数据库动态获取当前页的数据内容
        Page page = service.search(cid, pageNumber, countForPage, rname);

        //将从数据库中获取到的数据信息写回到客户端
        String s = mapper.writeValueAsString(page);
        response.getWriter().write(s);
    }

    /**
     * 详情页面
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void getParticulars(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rid = request.getParameter("rid");
        System.out.println("getParticulars中的rid为------" + rid);
        Route route = service.getRoute(Integer.parseInt(rid));
        System.out.println(route);
        writeValue(route,response);
       /* String r = mapper.writeValueAsString(route);
        response.getWriter().write(r);*/
    }

    /**
     * 判断用户是否收藏
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rid = request.getParameter("rid");
        User user = (User) request.getSession().getAttribute("user");
        int uid ;
        // 判断用户是否登录
        if (user == null){
            // 用户未登录
            uid = 0;
        }else {
            // 用户登录
            uid = user.getUid();
        }
        //调用FavoriteService中的方法,判断是否收藏
        Boolean flag = favoriteService.isFavorite(uid, Integer.parseInt(rid));
       writeValue(flag,response);
    }

    /**
     * 用户收藏操作
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void saveFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            //用户未登录登,则提示用户登录
            System.out.println("请登录");
            //再次不作操作,直接返回
            return;
        }

        // 获取线路的rid
        String rid = request.getParameter("rid");
        // 调用Favoriteservice中的添加方法
        favoriteService.saveFavorite(user.getUid(),Integer.parseInt(rid));
    }
}