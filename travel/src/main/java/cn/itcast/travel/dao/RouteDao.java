package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Route;

import java.util.List;

/**
 * 操作tab_route表
 */
public interface RouteDao {
    /**
     * 查询分类
     * @param cid
     * @return
     */
    List<Route> groupByCid(int cid);

    /**
     * 分页查询
     * @param cid
     * @param start
     * @param countForPage
     * @return
     */
    List<Route> groupByCid(int cid,int start,int countForPage);

    /**
     * 模糊查询,
     * @param rname
     * @return
     */
    List<Route> search_All(int cid,String rname);

    /**
     * 模糊查询
     * @param cid
     * @param start
     * @param countForPage
     * @param rname
     * @return
     */
    List<Route> search_list(int cid,int start,int countForPage,String rname);

    Route getRoute(int rid);

}
