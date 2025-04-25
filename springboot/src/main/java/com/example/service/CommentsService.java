package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Comments;
import com.example.mapper.CommentsMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 业务层方法
 */
@Service
public class CommentsService {

    @Resource
    private CommentsMapper commentsMapper;

    public void add(Comments comments) {
        comments.setTime(DateUtil.now());
        commentsMapper.insert(comments);
    }

    public void updateById(Comments comments) {
        commentsMapper.updateById(comments);
    }

    public void deleteById(Integer id) {
        commentsMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            commentsMapper.deleteById(id);
        }
    }

    public Comments selectById(Integer id) {
        return commentsMapper.selectById(id);
    }

    public List<Comments> selectAll(Comments comments) {
        return commentsMapper.selectAll(comments);
    }

    public PageInfo<Comments> selectPage(Comments comments, Integer pageNum, Integer pageSize) {
        Account account = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(account.getRole())) {
            comments.setStatus("通过");
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Comments> list = commentsMapper.selectAll(comments);
        return PageInfo.of(list);
    }

}
