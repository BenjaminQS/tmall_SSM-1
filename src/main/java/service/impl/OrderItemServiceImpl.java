package service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mapper.OrderItemMapper;
import pojo.Order;
import pojo.OrderItem;
import pojo.OrderItemExample;
import pojo.Product;
import service.OrderItemService;
import service.ProductService;

@Service
public class OrderItemServiceImpl implements OrderItemService {
	@Autowired
	OrderItemMapper orderItemMapper;
	@Autowired
	ProductService productService;

	@Override
	public void add(OrderItem c) {
		orderItemMapper.insert(c);
	}

	@Override
	public void delete(int id) {
		orderItemMapper.deleteByPrimaryKey(id);
	}

	@Override
	public void update(OrderItem c) {
		orderItemMapper.updateByPrimaryKeySelective(c);
	}

	@Override
	public OrderItem get(int id) {
		OrderItem oi = orderItemMapper.selectByPrimaryKey(id);
		setProduct(oi);
		return oi;
	}

	@Override
	public List list() {
		OrderItemExample example = new OrderItemExample();
		example.setOrderByClause("id desc");
		return orderItemMapper.selectByExample(example);
	}

	@Override
	public void fill(List<Order> os) {
		for (Order o : os) {
			fill(o);
		}
	}

	@Override
	public void fill(Order o) {
		OrderItemExample example = new OrderItemExample();
		example.createCriteria()
				.andOidEqualTo(o.getId());
		example.setOrderByClause("id desc");
		List<OrderItem> ois = orderItemMapper.selectByExample(example);
		setProduct(ois);
		
		float total = 0;
        int totalNumber = 0;
        for (OrderItem oi : ois) {
            total+=oi.getNumber()*oi.getProduct().getPromotePrice();
            totalNumber+=oi.getNumber();
        }
        o.setTotal(total);
        o.setTotalNumber(totalNumber);
        o.setOrderItems(ois);
	}
	
	public void setProduct(List<OrderItem> ois){
        for (OrderItem oi: ois) {
            setProduct(oi);
        }
    }
 
    private void setProduct(OrderItem oi) {
        Product p = productService.get(oi.getPid());
        oi.setProduct(p);
    }
	
}
