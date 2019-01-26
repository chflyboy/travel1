package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Seller;

/**
 * 查询卖家信息
 */
public interface SellerDao {
    /**
     * 通过sid查询卖家信息
     * @param sid
     * @return
     */
    Seller getSeller(int sid);
}
