package controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pojo.Category;
import pojo.Product;
import pojo.ProductImage;
import pojo.PropertyValue;
import pojo.Review;
import pojo.User;
import service.CategoryService;
import service.OrderItemService;
import service.OrderService;
import service.ProductImageService;
import service.ProductService;
import service.PropertyValueService;
import service.ReviewService;
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
    @Autowired
    ReviewService reviewService;
    //主页
    @RequestMapping("forehome")
    public String home(Model model) {
        List<Category> cs= categoryService.list();
        
        productService.fill(cs);
        productService.fillByRow(cs);
        
        model.addAttribute("cs", cs);
        return "fore/homePage";
    }
    //注册
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
    //登陆
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
    //退出
    @RequestMapping("forelogout")
    public String login(HttpSession session){
    	session.removeAttribute("user");
    	return "redirect:forehome";
    }
	
    //进入产品页
    @RequestMapping("foreproduct")
    public String product(Model model, int id){
    	//产品数据 包括展示图片 详情图片 销量和评价数量四个方面的设置
    	Product p = productService.get(id);
    	List<ProductImage> pisSingle = productImageService.list(p.getId(), productImageService.type_single);
    	List<ProductImage> pisDetail = productImageService.list(p.getId(), productImageService.type_detail);
    	p.setProductSingleImages(pisSingle);
    	p.setProductDetailImages(pisDetail);
    	productService.setSaleAndReviewNumber(p);
    	//产品属性值
    	List<PropertyValue> pvs = propertyValueService.list(p.getId());
    	//评价
    	List<Review> reviews = reviewService.list(p.getId());
    	
    	model.addAttribute("p", p);
    	model.addAttribute("pvs", pvs);
    	model.addAttribute("reviews", reviews);
    	
    	return "fore/productPage";
    }
    
}
