package com.example.demo.controller;

import com.example.demo.service.ProductService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class ProductController {

    private ProductService productService;
    private CacheManager cacheManager;

    @Autowired
    ProductController(ProductService productService, CacheManager cacheManager){
        this.productService = productService;
        this.cacheManager = cacheManager;
    }

    JedisPool jedisPool = new JedisPool();
    Jedis jedis = jedisPool.getResource();

    private final static Logger log = LoggerFactory.getLogger(ProductController.class);

    //web
    @GetMapping("getId")
    public ModelAndView getId(@RequestParam String userid, @RequestParam String pcode, ModelAndView mv){
        try {
            Cache cache = cacheManager.getCache("getMember");

            ArrayList<String> ehcacheKey = new ArrayList<>();
            ehcacheKey.add(userid);
            ehcacheKey.add(pcode);
            mv.setViewName("cache");
            mv.addObject("userid", userid);
            mv.addObject("pcode", pcode);

            String rediskey = "getMember::" + userid + "," + pcode;
            String redisValue = jedis.get(rediskey);

            //ehcache 조회
            if (!StringUtils.isEmpty(cache.get(ehcacheKey))) {
                mv.addObject("ehcacheVal", cache.get(ehcacheKey).get());
                return mv;
            }

            //redis 조회
            if (!StringUtils.isEmpty(redisValue)) {
                cache.put(ehcacheKey, redisValue);
                mv.addObject("redisValue", redisValue);
                return mv;
            } else {
                //db 조회
                List<Map<String, Object>> getDB = productService.getId(userid, pcode);
                jedis.set(rediskey, String.valueOf(getDB));
                cache.put(ehcacheKey, getDB);
                mv.addObject("getDB", getDB);
                return mv;
            }
        } catch (Exception e) {
            log.error("Error!!!! user Id Search Error >>>>{}", e);
            return null;
        }
    }

    //ehcache 삭제(web)
    @GetMapping("ehcacheDel")
    public String ehcacheDel(ModelAndView mv){
        Cache cache = cacheManager.getCache("getMember");
        cache.clear();
        return "redirect:getId?userid=&pcode=";
    }

    //redis cache 삭제(web)
    @GetMapping("redisDel")
    public String redisDel(){
        jedis.flushAll();
        return "redirect:getId?userid=&pcode=";
    }

    //mobile
    @GetMapping("getIdM")
    public ModelAndView getIdM(@RequestParam String userid, @RequestParam String pcode, ModelAndView mv){
        try{
            mv.setViewName("cacheM");
            mv.addObject("userid",userid);
            mv.addObject("pcode",pcode);

            Cache cache = cacheManager.getCache("getMemberM");
            ArrayList<String> cacheKeyM = new ArrayList<>();
            cacheKeyM.add(userid);
            cacheKeyM.add(pcode);

            String redisKeyM = "getMemberM::"+userid+","+pcode;
            String redisValueM = jedis.get(redisKeyM);

            //ehcache조회
            if(!StringUtils.isEmpty(cache.get(cacheKeyM))){
                mv.addObject("ehcachValM",cache.get(cacheKeyM).get());
                return mv;
            }

            //redis조회
            if(!StringUtils.isEmpty(redisValueM)){
                cache.put(cacheKeyM,redisValueM);
                mv.addObject("redisValueM",redisValueM);
                return mv;
            }else {
                //db 조회
                List<Map<String, Object>> getDBM = productService.getIdM(userid, pcode);
                jedis.set(redisKeyM, String.valueOf(getDBM));
                cache.put(cacheKeyM, getDBM);
                mv.addObject("getDBM", getDBM);
                return mv;
            }
        }catch (Exception e){
            log.error("Error!!!! user Id Search Error >>>>{}", e);
            return null;
        }
    }

    //ehcache 삭제(mobile)
    @GetMapping("ehcacheMDel")
    public String ehcacheMDel(){
        Cache cache = cacheManager.getCache("getMemberM");
        cache.clear();
        return "redirect:getIdM?userid=&pcode=";
    }

    //redis cache 삭제(mobile)
    @GetMapping("redisMDel")
    public String redisMDel(){
        jedis.flushAll();
        return "redirect:getIdM?userid=&pcode=";
    }
}