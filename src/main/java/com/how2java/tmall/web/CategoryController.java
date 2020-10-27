package com.how2java.tmall.web;

import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.service.CategoryService;
import com.how2java.tmall.util.ImageUtil;
import com.how2java.tmall.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

//表示这是一个控制器，并且对每个方法的返回值都会直接转换为 json 数据格式。
@RestController
public class CategoryController {
    @Autowired CategoryService categoryService;

    /*
    * 分类列表
    *
    * 对于categories 访问，会获取所有的 Category对象集合，并返回这个集合。
    * 因为是声明为 @RestController， 所以这个集合，又会被自动转换为 JSON数组抛给浏览器。
    * */
    @GetMapping("/categories")
    public Page4Navigator<Category> list(@RequestParam(value = "start", defaultValue = "0") int start,
                                         @RequestParam(value = "size", defaultValue = "5") int size) throws Exception {
        start = start<0?0:start;
        //5表示导航分页最多有5个，像 [1,2,3,4,5] 这样
        Page4Navigator<Category> page =categoryService.list(start, size, 5);
        return page;
    }

    @PostMapping("/categories")
    public Object add(Category bean, MultipartFile image, HttpServletRequest request) throws Exception {
        categoryService.add(bean);
        saveOrUpdateImageFile(bean, image, request);
        return bean;
    }

    /* 保存或修改图片 */
    public void saveOrUpdateImageFile(Category bean, MultipartFile image, HttpServletRequest request) throws IOException {
        //imageFolder：存放路径（img/category）
        File imageFolder= new File(request.getServletContext().getRealPath("img/category"));
        //file：文件名（id.jpg）
        File file = new File(imageFolder,bean.getId()+".jpg");
        //如果/img/category目录不存在，则创建该目录
        if(!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        //把UploadedImageFile（上传的照片）保存到 （img/category/id.jpg）
        image.transferTo(file);
        //通过ImageUtil.change2jpg(file); 确保图片格式一定是jpg，而不仅仅是后缀名是jpg.
        BufferedImage img = ImageUtil.change2jpg(file);
        //写入图片
        ImageIO.write(img, "jpg", file);
    }

    //首先根据id 删除数据库里的数据
    @DeleteMapping("/categories/{id}")
    public String delete(@PathVariable("id") int id, HttpServletRequest request)  throws Exception {
        categoryService.delete(id);
        //imageFolder：存放路径（img/category）
        File  imageFolder= new File(request.getServletContext().getRealPath("img/category"));
        //file：文件名（id.jpg）
        File file = new File(imageFolder,id+".jpg");
        //删除文件
        file.delete();
        return null;
    }

    @GetMapping("/categories/{id}")
    public Category get(@PathVariable("id") int id) throws Exception {
        Category bean=categoryService.get(id);
        return bean;
    }

    @PutMapping("/categories/{id}")
    public Object update(Category bean, MultipartFile image,HttpServletRequest request) throws Exception {
        //获取修改的分类名
        String name = request.getParameter("name");
        bean.setName(name);
        categoryService.update(bean);
        if(image!=null) {
            saveOrUpdateImageFile(bean, image, request);
        }
        return bean;
    }

}