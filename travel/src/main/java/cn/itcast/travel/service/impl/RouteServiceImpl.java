package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.RouteimgDao;
import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.dao.impl.RouteimgDaoImpl;
import cn.itcast.travel.dao.impl.SellerDaoImpl;
import cn.itcast.travel.domain.*;
import cn.itcast.travel.service.RouteService;

import java.util.List;

public class RouteServiceImpl implements RouteService {
    private RouteDao dao = new RouteDaoImpl();
    private RouteimgDao routeimg = new RouteimgDaoImpl();
    private SellerDao sellerDao = new SellerDaoImpl();
    private FavoriteDao favoriteDao = new FavoriteDaoImpl();

    /**
     * 获取该类的分页列表信息
     *
     * @param cid          从页面上传回的Route类的cid,可根据该cid获取数据库中的该类的所有数据
     * @param pageNumber   当前页码
     * @param countForPage 每页的条目数量
     * @return 分页信息封装对象后返回
     */
    @Override
    public Page listForPage(int cid, int pageNumber, int countForPage) {
        Page page = new Page();
        //总共条目
        List<Route> routes = dao.groupByCid(cid);
        int size = routes.size();
        int allPage;
        page.setAmount(size);
        //当前页页码
        page.setPageNumber(pageNumber);
        //每页数目
        page.setCountForPage(countForPage);
        //页码总数
        if (size % countForPage == 0) {
            allPage = size / countForPage;
        } else {
            allPage = size / countForPage + 1;
        }
        page.setAllPage(allPage);
        //获取当前页的列表信息
        int start = (pageNumber - 1) * countForPage;
        List<Route> list = dao.groupByCid(cid, start, countForPage);
        page.setRoutes(list);
        return page;
    }


    /**
     * 模糊查询,分页查询
     * @param cid
     * @param pageNumber
     * @param countForPage
     * @param rname
     * @return
     */
    public Page search(int cid, int pageNumber, int countForPage, String rname) {
        Page page = new Page();
        //总共条目
        List<Route> routes = dao.search_All(cid, rname);
        int size = routes.size();
        System.out.println("service层-------size:"+size);
        page.setAmount(size);
        //当前页页码
        page.setPageNumber(pageNumber);
        //每页数目
        page.setCountForPage(countForPage);
        //页码总数
        int allPage;
        if (routes.size() % countForPage == 0) {
            allPage = routes.size() / countForPage;
        } else {
            allPage = routes.size() / countForPage + 1;
        }
        page.setAllPage(allPage);
        //获取当前页的列表信息
        int start = (pageNumber - 1) * countForPage;
        System.out.println("service层-------start:"+start);
        List<Route> list = dao.search_list(cid, start, countForPage, rname);
        page.setRoutes(list);
        return page;
    }

    @Override
    public Route getRoute(int rid) {
        System.out.println("传入service的rid为:----"+rid);
        Route route = dao.getRoute(rid);
        System.out.println("查到的route----"+route);
        List<RouteImg> imgs = routeimg.getImgs(rid);
        System.out.println("查到的图片为:--------"+imgs);
        route.setRouteImgList(imgs);
        System.out.println("sid的值为:-----------"+route.getSid());
        Seller seller = sellerDao.getSeller(route.getSid());
        System.out.println("查到的卖家信息为:---------"+seller);
        route.setSeller(seller);
        int count = favoriteDao.count(rid);
        route.setCount(count);
        return route;
    }

    /**
     * 获取某个用户的所有收藏信息
     * @param user
     * @return
     */
    @Override
    public List<Route> getFavorites(User user) {
        List<Favorite> all = favoriteDao.findAll(user.getUid());
        for (Favorite favorite : all) {
            Route route = favorite.getRoute();
        }
        return null;
    }
}
