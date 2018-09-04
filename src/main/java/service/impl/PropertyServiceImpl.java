package service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mapper.PropertyMapper;
import pojo.Property;
import pojo.PropertyExample;
import service.PropertyService;

@Service
public class PropertyServiceImpl implements PropertyService{
	
	@Autowired
	PropertyMapper propertyMapper;

	@Override
	public void add(Property p) {
		propertyMapper.insert(p);
	}

	@Override
	public void delete(int id) {
		propertyMapper.deleteByPrimaryKey(id);
	}

	@Override
	public void update(Property p) {
		propertyMapper.updateByPrimaryKeySelective(p);
	}

	@Override
	public Property get(int id) {
		return propertyMapper.selectByPrimaryKey(id);
	}

	@Override
	public List list(int cid) {
		PropertyExample example = new PropertyExample();
		example.createCriteria().andCidEqualTo(cid);
		example.setOrderByClause("id desc");
		return propertyMapper.selectByExample(example);
	}

}
