package com.how2java.tmall.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.how2java.tmall.dao.PropertyDAO;
import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.pojo.Property;
import com.how2java.tmall.util.Page4Navigator;

@Service
public class PropertyService {

    @Autowired PropertyDAO propertyDAO;
    @Autowired CategoryService categoryService;

    public void add(Property bean) {
        propertyDAO.save(bean);
    }

    public void delete(int id) {
        propertyDAO.delete(id);
    }

    public Property get(int id) {
        return propertyDAO.findOne(id);
    }

    public void update(Property bean) {
        propertyDAO.save(bean);
    }

    public Page4Navigator<Property> list(int cid, int start, int size,int navigatePages) {
        Category category = categoryService.get(cid);
        //首先创建一个 Sort 对象，表示通过 id 倒排序， 然后通过 categoryDAO进行查询。
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page<Property> pageFromJPA =propertyDAO.findByCategory(category,pageable);
        return new Page4Navigator<>(pageFromJPA,navigatePages);
    }
}