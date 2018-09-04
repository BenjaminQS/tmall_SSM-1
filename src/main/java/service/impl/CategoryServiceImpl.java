package service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mapper.CategoryMapper;
import pojo.Category;
import service.CategoryService;
import util.Page;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	CategoryMapper categoryMapper;
	
	@Override
	public List<Category> list() {
		return categoryMapper.list();
	}

	@Override
	public int total() {
		return categoryMapper.total();
	}

	@Override
	public void add(Category category) {
		categoryMapper.add(category);
	}

	@Override
	public void delete(int id) {
		categoryMapper.delete(id);
	}

	@Override
	public Category get(int id) {
		return categoryMapper.get(id);
	}

	@Override
	public void update(Category category) {
		categoryMapper.update(category);
	}

}
