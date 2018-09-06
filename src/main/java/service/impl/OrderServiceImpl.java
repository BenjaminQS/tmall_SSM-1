package service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import mapper.OrderMapper;
import pojo.Order;
import pojo.OrderExample;
import pojo.OrderItem;
import pojo.User;
import service.OrderItemService;
import service.OrderService;
import service.UserService;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	OrderMapper orderMapper;
	@Autowired
    UserService userService;
	@Autowired
	OrderItemService orderItemService;

	@Override
	public void add(Order c) {
		orderMapper.insert(c);		
	}

	@Override
	public void delete(int id) {
		orderMapper.deleteByPrimaryKey(id);
	}

	@Override
	public void update(Order c) {
		orderMapper.updateByPrimaryKeySelective(c);
	}

	@Override
	public Order get(int id) {
		return orderMapper.selectByPrimaryKey(id);
	}

	@Override
	public List list() {
		OrderExample example =new OrderExample();
        example.setOrderByClause("id desc");
        List<Order> result =orderMapper.selectByExample(example);
        setUser(result);
        return result;
	}

	public void setUser(List<Order> os){
        for (Order o : os)
            setUser(o);
    }
    public void setUser(Order o){
        int uid = o.getUid();
        User u = userService.get(uid);
        o.setUser(u);
    }
    
    //注解进行事务管理
	@Override
	@Transactional(propagation= Propagation.REQUIRED,rollbackForClassName="Exception")
	public float add(Order o, List<OrderItem> ois) {
		float total = 0;
        add(o);	//添加订单
        
        for (OrderItem oi: ois) {
            oi.setOid(o.getId());
            orderItemService.update(oi);	//设置订单
            total+=oi.getProduct().getPromotePrice()*oi.getNumber();
        }
        
        return total;
	}
}
