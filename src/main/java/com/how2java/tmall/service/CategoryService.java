package com.how2java.tmall.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.how2java.tmall.dao.CategoryDAO;
import com.how2java.tmall.pojo.Category;

@Service
public class CategoryService {
    @Autowired CategoryDAO categoryDAO;

    public List<Category> list() {
        //首先创建一个 Sort 对象，表示通过 id 倒排序， 然后通过 categoryDAO进行查询。
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return categoryDAO.findAll(sort);
    }
}