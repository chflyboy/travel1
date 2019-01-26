package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import redis.clients.jedis.Jedis;

import java.util.List;

public class CategoryServiceImpl implements CategoryService {
    private CategoryDao dao = new CategoryDaoImpl();
    //先从缓存重获取集合
    private Jedis jedis = JedisUtil.getJedis();

    /**
     * 查询所有的导航条目
     *
     * @return
     */
    @Override
    public String findNav() {
        //从jedis池获取一个jedis
        String categories = jedis.get("categories");
        System.out.println("从缓存中取出的categories: " + categories);
        System.out.println(categories == null);
        if (categories == null || categories.length() == 0) {
            System.out.println("进入数据库");
            //如果缓存中没有查到导航类目,则从数据库中获取
            List<Category> list = dao.findNav();
            System.out.println("从数据库中获取的list: " + list);
            //将数据库中查到的导航类目列表序列化为json对象储存到缓存中
            ObjectMapper mapper = new ObjectMapper();
            try {
                categories = mapper.writeValueAsString(list);
                jedis.append("categories", categories);
                System.out.println("将list存入到缓存中");
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return categories;
        } else {
            return categories;
        }
    }
}
