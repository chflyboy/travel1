package cn.itcast.travel.dao;

import cn.itcast.travel.domain.RouteImg;

import java.util.List;

/**
 * 详情页面轮播图查询
 */
public interface RouteimgDao {
    /**
     * 通过rid获取轮播图
     * @param rid
     * @return
     */
    List<RouteImg> getImgs(int rid);
}
