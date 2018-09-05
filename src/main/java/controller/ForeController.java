package controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pojo.Category;
import pojo.User;
import service.CategoryService;
import service.OrderItemService;
import service.OrderService;
import service.ProductImageService;
import service.ProductService;
import service.PropertyValueService;
import service.UserService;

@Controller
@RequestMapping("")
public class ForeController {
	
	@Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;
    @Autowired
    UserService userService;
    @Autowired
    ProductImageService productImageService;
    @Autowired
    PropertyValueService propertyValueService;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderItemService orderItemService;
    
    @RequestMapping("forehome")
    public String home(Model model) {
        List<Category> cs= categoryService.list();
        
        productService.fill(cs);
        productService.fillByRow(cs);
        
        model.addAttribute("cs", cs);
        return "fore/homePage";
    }
    
    @RequestMapping("foreregister")
    public String register(Model model, User user){
    	if(userService.isExist(user.getName())){
    		String msg = "用户名已存在";
    		model.addAttribute("msg", msg);
    		
    		return "fore/registerPage";
    	}
    	
    	userService.add(user);
    	
    	return "fore/registerSuccessPage";
    }
    
    @RequestMapping("forelogin")
    public String login(Model model, @RequestParam("name") String name, @RequestParam("password") String password, HttpSession session){
    	User user = userService.get(name, password);
    	if(null == user){
    		model.addAttribute("msg", "账号密码错误");
    		return "fore/loginPage";
    	}
    	
    	session.setAttribute("user", user);
    	return "redirect:forehome";
    }
    
    @RequestMapping("forelogout")
    public String login(HttpSession session){
    	session.removeAttribute("user");
    	return "redirect:forehome";
    }
	
}
