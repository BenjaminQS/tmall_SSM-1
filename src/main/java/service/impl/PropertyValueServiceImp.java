package service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mapper.PropertyValueMapper;
import pojo.Category;
import pojo.Product;
import pojo.Property;
import pojo.PropertyValue;
import pojo.PropertyValueExample;
import service.PropertyService;
import service.PropertyValueService;

@Service
public class PropertyValueServiceImp implements PropertyValueService{
	@Autowired
	PropertyValueMapper propertyValueMapper;
	@Autowired
	PropertyService propertyService;

	@Override
	public void init(Product p) {
		List<Property> pts = propertyService.list(p.getCid());
		
		for (Property pt : pts) {
			PropertyValue pv = get(pt.getId(), p.getId());
			if(null == pv){
				pv = new PropertyValue();
                pv.setPid(p.getId());
                pv.setPtid(pt.getId());
                propertyValueMapper.insert(pv);
			}
		}
	}

	@Override
	public void update(PropertyValue pv) {
		propertyValueMapper.updateByPrimaryKeySelective(pv);
	}

	@Override
	public PropertyValue get(int ptid, int pid) {
		PropertyValueExample example = new PropertyValueExample();
        example.createCriteria().andPtidEqualTo(ptid).andPidEqualTo(pid);
        List<PropertyValue> pvs= propertyValueMapper.selectByExample(example);
        if (pvs.isEmpty())
            return null;
        return pvs.get(0);
	}

	@Override
	public List<PropertyValue> list(int pid) {
		PropertyValueExample example = new PropertyValueExample();
        example.createCriteria().andPidEqualTo(pid);
        List<PropertyValue> result = propertyValueMapper.selectByExample(example);
        for (PropertyValue pv : result) {
            Property property = propertyService.get(pv.getPtid());
            pv.setProperty(property);
        }
        return result;
	}
	
}
