package com.example.demo.controller;



import com.example.demo.service.ProductService;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping(value = "getId", produces = "application/json")
    @ResponseBody
    public ModelAndView getId(@RequestParam(value = "userid") String userid, @RequestParam(value = "pcode") String pcode, ModelAndView mv) {

        try {
            Cache cache = cacheManager.getCache("getMember");
            //캐시 키를 배열로 저장
            ArrayList<String> ehcacheKey = new ArrayList<>();
            ehcacheKey.add(userid);
            ehcacheKey.add(pcode);
            mv.setViewName("cache.jsp");
            mv.addObject("userid",userid);
            mv.addObject("pcode",pcode);

            if (cache.get(ehcacheKey) != null) {
                long startTime = System.currentTimeMillis();
                long elapsedTime = System.currentTimeMillis() - startTime;
                log.info(userid + "," + pcode + "   :::: SearchTime ::::  " + elapsedTime + "  :::::");
                mv.addObject("ehcacheKey", cache.get(ehcacheKey).get());
                return mv;
            }

            String rediskey = "getMember::"+userid+","+pcode;
            String redisValue = jedis.get(rediskey);

            if(redisValue != null){
                long startTime = System.currentTimeMillis();
                long elapsedTime = System.currentTimeMillis() - startTime;
                log.info(userid + "," + pcode + "   :::: SearchTime ::::  " + elapsedTime + "  :::::");
                mv.addObject("redisValue",redisValue);
                return mv;
            }
            List<Map<String, Object>> temp = productService.getId(userid, pcode);
            long startTime = System.currentTimeMillis();
            long elapsedTime = System.currentTimeMillis() - startTime;
            log.info(userid + "," + pcode + "   :::: SearchTime ::::  " + elapsedTime + "  :::::");
            jedis.set(rediskey, String.valueOf(temp));
            mv.addObject("temp",temp);
            return mv;


        } catch (Exception e) {
            log.error("Error!!!! user Id Search Error >>>>{}", e);
            return null;
        }


    }

    @GetMapping(value="getIdM", produces = "application/json")
    @ResponseBody
    public ModelAndView getIdM(@RequestParam("userid") String userid, @RequestParam("pcode") String pcode, ModelAndView mv){
        try{
            mv.setViewName("cacheM.jsp");
            mv.addObject("userid",userid);
            mv.addObject("pcode",pcode);

            Cache cache = cacheManager.getCache("getMemberM");
            ArrayList<String> cacheKey = new ArrayList<>();
            cacheKey.add(userid);
            cacheKey.add(pcode);
            if(cache.get(cacheKey) != null){
                mv.addObject("ehcachKeyM",cache.get(cacheKey).get());
                return mv;
            }

            String redisKey = "getMemberM::"+userid+","+pcode;
            String redisValue = jedis.get(redisKey);
            if(redisValue != null){
                mv.addObject("redisValueM",redisValue);
                return mv;
            }

            List<Map<String, Object>> temp = productService.getIdM(userid,pcode);
            jedis.set(redisKey, String.valueOf(temp));
            mv.addObject("tempM",temp);
            return mv;
        }catch (Exception e){
            log.error("Error!!!! user Id Search Error >>>>{}", e);
            return null;
        }
    }





}