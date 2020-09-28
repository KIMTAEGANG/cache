package com.example.demo.controller;



import com.example.demo.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

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

    JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
    JedisPool jedisPool = new JedisPool();
    Jedis jedis = jedisPool.getResource();

    private final static Logger log = LoggerFactory.getLogger(ProductController.class);

    //web
    @GetMapping(value = "getId", produces = "application/json")
    @ResponseBody
    public ModelAndView getId(@RequestParam(value = "userid") String userid, @RequestParam(value = "pcode") String pcode,
                              @RequestParam(value = "category") String category, ModelAndView mv) {

        try {
            long startTime = System.currentTimeMillis();
            long elapsedTime = System.currentTimeMillis() - startTime;
            log.info(userid + "," + pcode + "   :::: SearchTime ::::  " + elapsedTime + "  :::::");

            Cache cache = cacheManager.getCache("getMember");

            //캐시 키를 배열로 저장
            ArrayList<String> ehcacheKey = new ArrayList<>();
            ehcacheKey.add(userid);
            ehcacheKey.add(pcode);
            mv.setViewName("cache");
            mv.addObject("userid", userid);
            mv.addObject("pcode", pcode);


            String rediskey = "getMember::" + userid + "," + pcode;
            String redisValue = jedis.get(rediskey);

            if (category == "") {


                if (cache.get(ehcacheKey) != null) {
                    mv.addObject("ehcacheKey", cache.get(ehcacheKey).get());
                    return mv;
                }

                if (redisValue != null) {
                    cache.put(ehcacheKey, redisValue);
                    mv.addObject("redisValue", redisValue);
                    return mv;
                }

                if ("".equals(userid) && "".equals(pcode)) {
                    return mv;
                } else {
                    List<Map<String, Object>> temp = productService.getId(userid, pcode);
                    jedis.set(rediskey, String.valueOf(temp));
                    cache.put(ehcacheKey, temp);
                    mv.addObject("temp", temp);
                    return mv;
                }
            } else if ("total".equals(category)){
                if(cache.get(ehcacheKey) != null)
                mv.addObject("ehcacheKey", cache.get(ehcacheKey).get());

                if(redisValue != null)
                mv.addObject("redisValue", redisValue);

                List<Map<String, Object>> temp = productService.getId(userid, pcode);
                mv.addObject("temp", temp);

                return mv;
            }
            return mv;

        } catch (Exception e) {
            log.error("Error!!!! user Id Search Error >>>>{}", e);
            return null;
        }
    }

    @RequestMapping("ehcacheDel")
    public String ehcacheDel(ModelAndView mv){
        Cache cache = cacheManager.getCache("getMember");
        cache.clear();
        return "redirect:getId?userid=&pcode=&category=";
    }

    @RequestMapping("redisDel")
    public String redisDel(){
        jedis.flushAll();
        return "redirect:getId?userid=&pcode=&category=";
    }


    //Mobile
    @GetMapping(value="getIdM", produces = "application/json")
    @ResponseBody
    public ModelAndView getIdM(@RequestParam("userid") String userid, @RequestParam("pcode") String pcode,
                               @RequestParam("category") String category, ModelAndView mv){
        try{
            mv.setViewName("cacheM");
            mv.addObject("userid",userid);
            mv.addObject("pcode",pcode);

            Cache cache = cacheManager.getCache("getMemberM");
            ArrayList<String> cacheKey = new ArrayList<>();
            cacheKey.add(userid);
            cacheKey.add(pcode);

            String redisKey = "getMemberM::"+userid+","+pcode;
            String redisValue = jedis.get(redisKey);


            if(category == ""){
                if(cache.get(cacheKey) != null){
                    mv.addObject("ehcachKeyM",cache.get(cacheKey).get());
                    return mv;
                }

                if(redisValue != null){
                    cache.put(cacheKey,redisValue);
                    mv.addObject("redisValueM",redisValue);
                    return mv;
                }
                if("".equals(userid) && "".equals(pcode)){
                    return mv;
                }else {
                    List<Map<String, Object>> temp = productService.getIdM(userid, pcode);
                    jedis.set(redisKey, String.valueOf(temp));
                    cache.put(cacheKey, temp);
                    mv.addObject("tempM", temp);
                    return mv;
                }
            } else if("total".equals(category)){
                if(cache.get(cacheKey) != null)
                    mv.addObject("ehcachKeyM", cache.get(cacheKey).get());

                if(redisValue != null)
                    mv.addObject("redisValueM", redisValue);

                List<Map<String, Object>> temp = productService.getId(userid, pcode);
                mv.addObject("tempM", temp);

                return mv;
            }
            return mv;
        }catch (Exception e){
            log.error("Error!!!! user Id Search Error >>>>{}", e);
            return null;
        }
    }

    @RequestMapping("ehcacheMDel")
    public String ehcacheMDel(){
        Cache cache = cacheManager.getCache("getMemberM");
        cache.clear();
        return "redirect:getIdM?userid=&pcode=&category=";
    }

    @RequestMapping("redisMDel")
    public String redisMDel(){
        jedis.flushAll();
        return "redirect:getIdM?userid=&pcode=&category=";
    }
}