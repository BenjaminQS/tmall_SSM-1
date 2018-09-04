package controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import pojo.Category;
import service.CategoryService;
import util.ImageUtil;
import util.Page;
import util.UploadedImageFile;

@Controller
@RequestMapping("")
public class CategoryController {
	
	@Autowired
    CategoryService categoryService;
	
	@RequestMapping("admin_category_list")
	public String list(Model model, Page page){
		//通过分页插件指定分页参数
		PageHelper.offsetPage(page.getStart(), page.getCount());
		//调用list() 获取对应分页的数据
		List<Category> cs = categoryService.list();
		//通过PageInfo获取总数
		int total = (int) new PageInfo<>(cs).getTotal();
//		List<Category> cs= categoryService.list(page);
//		int total = categoryService.total();
		page.setTotal(total);
        model.addAttribute("cs", cs);
        model.addAttribute("page",page);
        return "admin/listCategory";
	}
	
	@RequestMapping("admin_category_add")
	public String add(Category c, HttpSession session, UploadedImageFile uploadedImageFile) throws IllegalStateException, IOException{
		
		categoryService.add(c);
		
		//创建目录
		File imageFolder= new File(session.getServletContext().getRealPath("img/category"));
	    File file = new File(imageFolder,c.getId()+".jpg");
	    if(!file.getParentFile().exists())
	        file.getParentFile().mkdirs();
	    //就这么一句话就获取了图片并且写到文件中
	    uploadedImageFile.getImg().transferTo(file);
		//转成jpg
	    BufferedImage img = ImageUtil.change2jpg(file);
	    ImageIO.write(img, "jpg", file);
	    
		return "redirect:/admin_category_list";
	}
	
	@RequestMapping("admin_category_delete")
	public String delete(int id, HttpSession session){
		
		categoryService.delete(id);
		
		File imageFolder= new File(session.getServletContext().getRealPath("img/category"));
	    File file = new File(imageFolder,id+".jpg");
	    file.delete();
		
		return  "redirect:/admin_category_list";
	}
	
	@RequestMapping("admin_category_edit")
	public String edit(Model model, int id){
		
		Category c = categoryService.get(id);
		
		model.addAttribute("c",c);
		
		return  "admin/editCategory";
	}
	
	@RequestMapping("admin_category_update")
	public String update(Category c, UploadedImageFile uploadedImageFile, HttpSession session) throws IllegalStateException, IOException{
		
		categoryService.update(c);
		
		MultipartFile image = uploadedImageFile.getImg();
		if(null != image && !image.isEmpty()){
			File imageFolder = new File(session.getServletContext().getRealPath("img/category"));
			File file = new File(imageFolder, c.getId()+".jpg");
			image.transferTo(file);
			BufferedImage img = ImageUtil.change2jpg(file);
		    ImageIO.write(img, "jpg", file);
		}
		
		return "redirect:admin_category_list";
	}
	
}
