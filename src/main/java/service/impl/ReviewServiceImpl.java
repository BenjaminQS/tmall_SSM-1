package service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mapper.ReviewMapper;
import pojo.Review;
import pojo.ReviewExample;
import pojo.User;
import service.ReviewService;
import service.UserService;

@Service
public class ReviewServiceImpl implements ReviewService {

	@Autowired
	ReviewMapper reviewMapper;
	@Autowired
	UserService userService;
	
	@Override
	public void add(Review c) {
		reviewMapper.insert(c);
	}

	@Override
	public void delete(int id) {
		reviewMapper.deleteByPrimaryKey(id);
	}

	@Override
	public void update(Review c) {
		reviewMapper.updateByPrimaryKeySelective(c);
	}

	@Override
	public Review get(int id) {
		return reviewMapper.selectByPrimaryKey(id);
	}

	@Override
	public List list(int pid) {
		ReviewExample example = new ReviewExample();
		example.createCriteria().andPidEqualTo(pid);
		example.setOrderByClause("id desc");
		
		List<Review> results = reviewMapper.selectByExample(example);
		
		setUser(results);
		
		return results;
	}

	@Override
	public int getCount(int pid) {
		return list(pid).size();
	}
	
	//给评价设置用户
	public void setUser(List<Review> reviews){
        for (Review review : reviews) {
            setUser(review);
        }
    }
    private void setUser(Review review) {
        int uid = review.getUid();
        User user =userService.get(uid);
        review.setUser(user);
    }

}
