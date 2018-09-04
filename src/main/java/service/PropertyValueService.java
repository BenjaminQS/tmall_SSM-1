package service;

import java.util.List;

import pojo.Product;
import pojo.PropertyValue;

public interface PropertyValueService {
	
	void init(Product p);
	
    void update(PropertyValue pv);
    PropertyValue get(int ptid, int pid);
    List<PropertyValue> list(int pid);
    
}
