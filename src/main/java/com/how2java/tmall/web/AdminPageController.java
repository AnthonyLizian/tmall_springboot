package com.how2java.tmall.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPageController {

    /* 访问地址 admin,就会客户端跳转到 admin_category_list去 */
    @GetMapping(value="/admin")
    public String admin(){
        return "redirect:admin_category_list";
    }

    /* 访问地址 admin_category_list 就会访问 admin/listCategory.html 文件 */
    @GetMapping(value="/admin_category_list")
    public String listCategory(){
        return "admin/listCategory";
    }
}