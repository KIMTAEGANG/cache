package com.example.demo.service;

import java.util.List;
import java.util.Map;

public interface ProductService {
    List<Map <String, Object>> getId(String userid, String pcode);
}