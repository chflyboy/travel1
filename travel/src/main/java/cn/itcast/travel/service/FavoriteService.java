package cn.itcast.travel.service;

public interface FavoriteService {

    Boolean isFavorite(int uid, int rid);

    void saveFavorite(int uid, int rid);

}
