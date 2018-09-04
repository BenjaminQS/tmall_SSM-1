package controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import pojo.Category;
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
	
}
