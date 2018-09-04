package service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mapper.ProductMapper;
import pojo.Category;
import pojo.Product;
import pojo.ProductExample;
import service.CategoryService;
import service.ProductService;

@Service
public class ProductServiceImpl implements ProductService{
	
	@Autowired
	ProductMapper productMapper;
	@Autowired
	CategoryService categoryService;

	@Override
	public void add(Product p) {
		productMapper.insert(p);
	}

	@Override
	public void delete(int id) {
		productMapper.deleteByPrimaryKey(id);
	}

	@Override
	public void update(Product p) {
		productMapper.updateByPrimaryKeySelective(p);
	}

	@Override
	public Product get(int id) {
		Product p = productMapper.selectByPrimaryKey(id);
		setCategory(p);
		return p;
	}

	@Override
	public List list(int cid) {
		ProductExample example = new ProductExample();
        example.createCriteria().andCidEqualTo(cid);
        example.setOrderByClause("id desc");
        List result = productMapper.selectByExample(example);
        setCategory(result);
		return result;
	}
	
	public void setCategory(List<Product> ps){
        for (Product p : ps)
            setCategory(p);
    }
    public void setCategory(Product p){
        int cid = p.getCid();
        Category c = categoryService.get(cid);
        p.setCategory(c);
    }

}
