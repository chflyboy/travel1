package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Favorite;

import java.util.List;

/**
 * 查询收藏表
 */
public interface FavoriteDao {
    /**
     * 根据cid和rid查询是否收藏过
     * @param uid
     * @param rid
     * @return
     */
    Favorite isFavorite(int uid,int rid);

    /**
     * 储存收藏
     * @param uid
     * @param rid
     */
    void saveFavorite(int uid,int rid);

    /**
     * 获取单条线路的收藏数量
     * @param rid
     * @return
     */
    int count(int rid);

    /**
     * 获取某个用户的所有收藏线路信息
     * @param uid
     * @return
     */
    List<Favorite> findAll(int uid);
}
