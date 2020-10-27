package com.how2java.tmall.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPageController {

    //admin
    /* 访问地址 admin,就会客户端跳转到 admin_category_list去 */
    @GetMapping(value="/admin")
    public String admin(){return "redirect:admin_category_list";}

    //category
    /* 访问地址 admin_category_list 就会访问 admin/listCategory.html 文件 */
    @GetMapping(value="/admin_category_list")
    public String listCategory(){return "admin/listCategory";}

    @GetMapping(value="/admin_category_edit")
    public String editCategory(){return "admin/editCategory";}

    //property
    @GetMapping(value="/admin_property_list")
    public String listProperty(){return "admin/listProperty";}

    @GetMapping(value="/admin_property_edit")
    public String editProperty(){return "admin/editProperty";}

    //product
    @GetMapping(value="/admin_product_list")
    public String listProduct(){return "admin/listProduct";}

    @GetMapping(value="/admin_product_edit")
    public String editProduct(){return "admin/editProduct";}

    //productImage
    @GetMapping(value="/admin_productImage_list")
    public String listProductImage(){return "admin/listProductImage";}

    //propertyValue
    @GetMapping(value="/admin_propertyValue_edit")
    public String editPropertyValue(){return "admin/editPropertyValue";}

    //order
    @GetMapping(value="/admin_order_list")
    public String listOrder(){return "admin/listOrder";}

    //user
    @GetMapping(value="/admin_user_list")
    public String listUser(){return "admin/listUser";}

}