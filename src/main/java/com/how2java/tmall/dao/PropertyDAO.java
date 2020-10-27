package com.how2java.tmall.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.pojo.Property;

public interface PropertyDAO extends JpaRepository<Property,Integer>{
    //根据分类进行查询
    Page<Property> findByCategory(Category category, Pageable pageable);
}