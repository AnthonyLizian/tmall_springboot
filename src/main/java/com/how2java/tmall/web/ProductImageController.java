package com.how2java.tmall.web;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.pojo.ProductImage;
import com.how2java.tmall.service.CategoryService;
import com.how2java.tmall.service.ProductImageService;
import com.how2java.tmall.service.ProductService;
import com.how2java.tmall.util.ImageUtil;

@RestController
public class ProductImageController {
    @Autowired ProductService productService;
    @Autowired ProductImageService productImageService;
    @Autowired CategoryService categoryService;

    @GetMapping("/products/{pid}/productImages")
    public List<ProductImage> list(@RequestParam("type") String type,
                                   @PathVariable("pid") int pid) throws Exception {
        Product product = productService.get(pid);

        if(ProductImageService.type_single.equals(type)) {
            List<ProductImage> singles =  productImageService.listSingleProductImages(product);
            return singles;
        }
        else if(ProductImageService.type_detail.equals(type)) {
            List<ProductImage> details =  productImageService.listDetailProductImages(product);
            return details;
        }
        else {
            return new ArrayList<>();
        }
    }

    @PostMapping("/productImages")
    public Object add(@RequestParam("pid") int pid,
                      @RequestParam("type") String type,
                      MultipartFile image, HttpServletRequest request) throws Exception {
        ProductImage bean = new ProductImage();
        Product product = productService.get(pid);
        bean.setProduct(product);
        bean.setType(type);
        productImageService.add(bean);

        //判断图片类型是单个图片还是详情图片
        //单个图片 img/productSingle
        //详情图片 img/productDetail
        String folder = "img/";
        if(ProductImageService.type_single.equals(bean.getType())){
            folder +="productSingle";
        }
        else{
            folder +="productDetail";
        }
        //imageFolder：存放路径
        File  imageFolder= new File(request.getServletContext().getRealPath(folder));
        //file：文件名（id.jpg）
        File file = new File(imageFolder,bean.getId()+".jpg");
        String fileName = file.getName();
        //如果目录不存在，则创建该目录
        if(!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        try {
            //把UploadedImageFile（上传的照片）保存到 img/productSingle 或者 img/productDetail
            image.transferTo(file);
            //通过ImageUtil.change2jpg(file); 确保图片格式一定是jpg，而不仅仅是后缀名是jpg.
            BufferedImage img = ImageUtil.change2jpg(file);
            //写入图片
            ImageIO.write(img, "jpg", file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //如果图片类型是单个图片 同时创建小图和中图
        if(ProductImageService.type_single.equals(bean.getType())){
            //imageFolder_small:小图存放路径
            String imageFolder_small= request.getServletContext().getRealPath("img/productSingle_small");
            //imageFolder_middle:中图存放路径
            String imageFolder_middle= request.getServletContext().getRealPath("img/productSingle_middle");
            //小图文件:img/productSingle_small/id.jpg
            File f_small = new File(imageFolder_small, fileName);
            //中图文件:img/productSingle_middle/id.jpg
            File f_middle = new File(imageFolder_middle, fileName);
            //创建小图文件
            f_small.getParentFile().mkdirs();
            //创建中图文件
            f_middle.getParentFile().mkdirs();
            //把图片改变大小后写入文件
            ImageUtil.resizeImage(file, 56, 56, f_small);
            ImageUtil.resizeImage(file, 217, 190, f_middle);
        }

        return bean;
    }

    @DeleteMapping("/productImages/{id}")
    public String delete(@PathVariable("id") int id, HttpServletRequest request)  throws Exception {
        ProductImage bean = productImageService.get(id);
        productImageService.delete(id);

        //判断图片类型是单个图片还是详情图片
        //单个图片 img/productSingle
        //详情图片 img/productDetail
        String folder = "img/";
        if(ProductImageService.type_single.equals(bean.getType()))
            folder +="productSingle";
        else
            folder +="productDetail";

        //imageFolder：存放路径
        File  imageFolder= new File(request.getServletContext().getRealPath(folder));
        //file：文件名（id.jpg）
        File file = new File(imageFolder,bean.getId()+".jpg");
        String fileName = file.getName();
        //删除文件
        file.delete();

        //如果图片类型是单个图片 同时删除小图和中图
        if(ProductImageService.type_single.equals(bean.getType())){
            String imageFolder_small= request.getServletContext().getRealPath("img/productSingle_small");
            String imageFolder_middle= request.getServletContext().getRealPath("img/productSingle_middle");
            File f_small = new File(imageFolder_small, fileName);
            File f_middle = new File(imageFolder_middle, fileName);
            f_small.delete();
            f_middle.delete();
        }

        return null;
    }

}