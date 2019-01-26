package cn.itcast.travel.service;

import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.Page;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;

import java.util.List;

public interface RouteService {

    /**
     * 分组查询
     * @param cid
     * @return
     */
    Page listForPage(int cid, int pageNumber, int countForPage);

    /**
     * 模糊查询
     * 分组查询
     * @param cid
     * @param pageNumber
     * @param countForPage
     * @param rname
     * @return
     */
    Page search(int cid, int pageNumber, int countForPage,String rname);

    /**
     * 获取某条线路的详细信息
     * @param rid
     * @return
     */
    Route getRoute(int rid);

    List<Route> getFavorites(User user);
}
