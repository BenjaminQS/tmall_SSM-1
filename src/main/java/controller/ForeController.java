package controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import com.github.pagehelper.PageHelper;

import comparator.ProductAllComparator;
import comparator.ProductDateComparator;
import comparator.ProductPriceComparator;
import comparator.ProductReviewComparator;
import comparator.ProductSaleCountComparator;
import pojo.Category;
import pojo.Order;
import pojo.OrderItem;
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
    
    //检查是否登陆 用于模态登陆窗口
    @RequestMapping("forecheckLogin")
    @ResponseBody
    public String checkLogin(HttpSession session){
    	User user = (User) session.getAttribute("user");
    	if(null != user)
    		return "success";
    	return "fail";
    }
    
    //模态窗口登陆
    @ResponseBody
    @RequestMapping("foreloginAjax")
    public String loginAjax(HttpSession session, @RequestParam("name") String name, @RequestParam("password") String password){
    	name = HtmlUtils.htmlEscape(name);
        User user = userService.get(name,password);
        
        if(null==user){
            return "fail";
        }
        session.setAttribute("user", user);
        return "success";
    }
    
    //分类页
    @RequestMapping("forecategory")
    public String category(Model model, int id, String sort){
    	Category c = categoryService.get(id);
    	productService.fill(c);
        productService.setSaleAndReviewNumber(c.getProducts());

        if(null!=sort){
            switch(sort){
                case "review":
                    Collections.sort(c.getProducts(),new ProductReviewComparator());
                    break;
                case "date" :
                    Collections.sort(c.getProducts(),new ProductDateComparator());
                    break;
 
                case "saleCount" :
                    Collections.sort(c.getProducts(),new ProductSaleCountComparator());
                    break;
 
                case "price":
                    Collections.sort(c.getProducts(),new ProductPriceComparator());
                    break;
 
                case "all":
                    Collections.sort(c.getProducts(),new ProductAllComparator());
                    break;
            }
        }
        
    	model.addAttribute("c", c);
    	
    	return "fore/categoryPage";
    }
    
    //搜索页
    @RequestMapping("foresearch")
    public String search(Model model, String keyword){
    	PageHelper.offsetPage(0,20);
    	List<Product> ps = productService.search(keyword);
    	productService.setSaleAndReviewNumber(ps);
    	
    	model.addAttribute("ps", ps);
    	
    	return "fore/searchResultPage";
    }
    
    //立即购买
    @RequestMapping("forebuyone")
    public String buyone(int pid, int num, HttpSession session){
    	User user = (User) session.getAttribute("user");
    	Product p = productService.get(pid);
    	
    	int oiid = 0;
    	boolean found = false;
    	List<OrderItem> ois = orderItemService.listByUser(user.getId());
    	for (OrderItem oi : ois) {
			if(oi.getPid().intValue() == pid){
				oi.setNumber(oi.getNumber() + num);
				orderItemService.update(oi);
				oiid = oi.getId();
				found = true;
				break;
			}
		}
    	if(!found){
    		OrderItem oi = new OrderItem();
        	oi.setNumber(num);
        	oi.setPid(pid);
        	oi.setUid(user.getId());
        	orderItemService.add(oi);
        	oiid = oi.getId();
    	}
    	
    	return "redirect:foresettle?oiids="+oiid;
    }
    
    //加入购物车
    @RequestMapping("foreaddCart")
    @ResponseBody
    public String addCart(int pid, int num, HttpSession session){
    	User user = (User) session.getAttribute("user");
    	Product p = productService.get(pid);
    	
    	boolean found = false;
    	List<OrderItem> ois = orderItemService.listByUser(user.getId());
    	for (OrderItem oi : ois) {
			if(oi.getPid().intValue() == pid){
				oi.setNumber(oi.getNumber() + num);
				orderItemService.update(oi);
				found = true;
				break;
			}
		}
    	if(!found){
    		OrderItem oi = new OrderItem();
        	oi.setNumber(num);
        	oi.setPid(pid);
        	oi.setUid(user.getId());
        	orderItemService.add(oi);
    	}
    	
    	return "success";
    }
    
    //结算页
    @RequestMapping("foresettle")
    public String settle(Model model, String[] oiids, HttpSession session){
    	List<OrderItem> ois = new ArrayList<>();
    	float total = 0;
    	for (String strid: oiids) {
    		int oiid = Integer.parseInt(strid);
			OrderItem oi = orderItemService.get(oiid);
			ois.add(oi);
			total += oi.getProduct().getPromotePrice() * oi.getNumber();
		}
    	
    	session.setAttribute("ois", ois);	//为什么用session
    	model.addAttribute("total", total);
    	
    	return "fore/settleAccountPage";
    }
    
    //购物车
    @RequestMapping("forecart")
    public String cart(Model model, HttpSession session){
    	User user = (User) session.getAttribute("user");
    	List<OrderItem> ois = orderItemService.listByUser(user.getId());
    	
    	model.addAttribute("ois", ois);
    	
    	return "fore/shoppingcartPage";
    }
    
    //修改购物车订单数量
    @RequestMapping("forechangeOrderItem")
    @ResponseBody
    public String changeOrderItem(int pid, int num, HttpSession session){
    	User user = (User) session.getAttribute("user");
    	if(null==user)
            return "fail";
    	
    	List<OrderItem> ois = orderItemService.listByUser(user.getId());
    	for (OrderItem oi : ois) {
            if(oi.getProduct().getId().intValue()==pid){
                oi.setNumber(num);
                orderItemService.update(oi);
                break;
            }
        }
    	
    	return "success";
    }
    
    //删除购物车订单项
    @RequestMapping("foredeleteOrderItem")
    @ResponseBody
    public String deleteOrderItem(int id, HttpSession session){
    	User user = (User) session.getAttribute("user");
    	if(null==user)
            return "fail";
    	
    	orderItemService.delete(id);
    	return "success";
    }
    
    //生成订单 跳转到支付页
    @RequestMapping("forecreateOrder")
    public String createOrder(Model model, Order order, HttpSession session){
    	User user =(User)  session.getAttribute("user");
    	
    	String orderCode = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + RandomUtils.nextInt(10000);
    	order.setOrderCode(orderCode);
        order.setCreateDate(new Date());
        order.setUid(user.getId());
        order.setStatus(OrderService.waitPay);
        
        List<OrderItem> ois= (List<OrderItem>) session.getAttribute("ois");	//显示结算的的时候 把订单项放到了session中
        
        float total =orderService.add(order,ois);
    	
    	return "redirect:forepay?oid="+order.getId() +"&total="+total;
    }
    
    @RequestMapping("forepaysuccess")
    public String paysuccess(int oid, float total, Model model) {
        Order order = orderService.get(oid);
        order.setStatus(OrderService.waitDelivery);
        order.setPayDate(new Date());
        orderService.update(order);
        
        model.addAttribute("o", order);
        
        return "fore/paySuccessPage";
    }
    
}
