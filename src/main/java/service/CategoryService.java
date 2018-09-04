package service;

import java.util.List;
import pojo.Category;
import util.Page;

public interface CategoryService {
	public List<Category> list();
	
	public void add(Category category);
	
	public void delete(int id);
	
	public Category get(int id);
	
	public void update(Category category);
}
