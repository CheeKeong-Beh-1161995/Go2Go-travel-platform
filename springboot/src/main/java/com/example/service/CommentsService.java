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
 * Business-level methods
 */
@Service
public class CommentsService {

    @Resource
    private CommentsMapper commentsMapper;

    public void add(Comments comments) {
        comments.setTime(DateUtil.now());
        Account currentUser = TokenUtils.getCurrentUser();
        comments.setUserId(currentUser.getId());
        commentsMapper.insert(comments);
        if (comments.getPid() != null) {
            Comments parentComment = this.selectById(comments.getPid());
            comments.setRootId(parentComment.getRootId());
        }else {
            comments.setRootId(comments.getId());
        }
        this.updateById(comments);
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
        PageHelper.startPage(pageNum, pageSize);
        List<Comments> list = commentsMapper.selectAll(comments);
        return PageInfo.of(list);
    }

    public Integer selectCount(Integer fid, String module) {
        return commentsMapper.selectCount(fid,module);
    }

    public PageInfo<Comments> selectTree(Comments comment, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Comments> list = commentsMapper.selectRoot(comment);  // 查询一级节点
        for (Comments root : list) {
            List<Comments> children = commentsMapper.selectByRootId(root.getId());
            root.setChildren(children);
        }
        return PageInfo.of(list);
    }

}
