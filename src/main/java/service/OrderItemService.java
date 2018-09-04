package service;

import java.util.List;

import pojo.Order;
import pojo.OrderItem;

public interface OrderItemService {
	
	void add(OrderItem c);
    void delete(int id);
    void update(OrderItem c);
    OrderItem get(int id);
    List list();
    
    void fill(List<Order> os);
    void fill(Order o);
}