package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Category;

import java.util.List;

public interface CategoryDao {
    /**
     * 查询导航条内容
     * @return
     */
    List<Category> findNav();
}
