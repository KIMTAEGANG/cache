package com.example.demo.controller;


import com.example.demo.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class ProductController {

    private ProductService productService;

    @Autowired
    ProductController(ProductService productService) {
        this.productService = productService;
    }

    private final static Logger log = LoggerFactory.getLogger(ProductController.class);

    @GetMapping(value = "getId", produces = "application/json")
    @ResponseBody
    public List<Map<String, Object>> getId(@RequestParam(value = "userid") String userid, @RequestParam(value = "pcode") String pcode) {
        try {
            long startTime = System.currentTimeMillis();
            List<Map<String,Object>> temp = productService.getId(userid,pcode);
            long elapsedTime = System.currentTimeMillis() - startTime;
            log.info(userid + "," + pcode + "   :::: SearchTime ::::  " + elapsedTime + "  :::::");
            return temp;

        } catch (Exception e) {
            log.error("Error!!!! user Id Search Error >>>>{}", e);
            return null;
        }



    }

}