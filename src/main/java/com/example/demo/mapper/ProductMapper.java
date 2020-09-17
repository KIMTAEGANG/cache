package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface ProductMapper {
    //@Cacheable(cacheNames = "member",key="#cacheKey",unless = "#result==null")
    List<Map<String,Object>> getId(String userid, String pcode);
}