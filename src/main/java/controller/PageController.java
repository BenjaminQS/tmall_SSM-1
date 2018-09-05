package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/*
 * WEB-INF目录下的资源是不能直接访问的 需要做这么一个控制器来做服务器跳转
 */

@Controller
@RequestMapping("")
public class PageController {

	@RequestMapping("registerPage")
	public String registerPage(){
		return "fore/registerPage";
	}
	
	
	@RequestMapping("registerSuccessPage")
	public String registerSuccessPage(){
		return "fore/registerSuccessPage";
	}
	
	@RequestMapping("loginPage")
    public String loginPage() {
        return "fore/login";
    }
	
    @RequestMapping("forealipay")
    public String alipay(){
        return "fore/alipay";
    }
	
}
