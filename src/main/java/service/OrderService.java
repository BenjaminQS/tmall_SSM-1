package service;

import java.util.List;

import pojo.Order;
import pojo.OrderItem;

public interface OrderService {
	String waitPay = "waitPay";
    String waitDelivery = "waitDelivery";
    String waitConfirm = "waitConfirm";
    String waitReview = "waitReview";
    String finish = "finish";
    String delete = "delete";
    
    void add(Order c);
    void delete(int id);
    void update(Order c);
    Order get(int id);
    List list();
    
    //事务添加订单
    float add(Order c,List<OrderItem> ois);
    //查询某用户未删除订单
    List list(int uid, String excludedStatus);
}
