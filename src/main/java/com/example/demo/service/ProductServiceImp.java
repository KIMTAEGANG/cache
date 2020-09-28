package com.example.demo.service;

import com.example.demo.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImp implements ProductService {

    private final ProductMapper productMapper;

    @Autowired
    ProductServiceImp(
            ProductMapper productMapper
    ) {
        this.productMapper = productMapper;
    }


    @Override
    @Cacheable(cacheNames = "getMember", key = "{#userid, #pcode}", unless = "#result == null")
    public List<Map<String, Object>> getId(String userid, String pcode) {
        return productMapper.getId(userid, pcode);
    }

    @Override
    @Cacheable(cacheNames = "getMemberM", key = "{#userid, #pcode}", unless = "#result == null")
    public List<Map<String, Object>> getIdM(String userid, String pcode) {
        return productMapper.getIdM(userid, pcode);
    }
}