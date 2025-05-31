package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Review;
import com.example.mapper.ReviewMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Business-level methods
 */
@Service
public class ReviewService {

    @Resource
    private ReviewMapper reviewMapper;

    public void add(Review review) {
        Account currentUser = TokenUtils.getCurrentUser();
        review.setTime(DateUtil.now());;
        review.setUserId(currentUser.getId());
        review.setUserName(currentUser.getUsername());
        reviewMapper.insert(review);
    }

    public void updateById(Review review) {
        reviewMapper.updateById(review);
    }

    public void deleteById(Integer id) {
        reviewMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            reviewMapper.deleteById(id);
        }
    }

    public Review selectById(Integer id) {
        return reviewMapper.selectById(id);
    }

    public List<Review> selectAll(Review review) {
        return reviewMapper.selectAll(review);
    }

    public PageInfo<Review> selectPage(Review review, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Review> list = reviewMapper.selectAll(review);
        return PageInfo.of(list);
    }

}
