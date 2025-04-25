package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Article;
import com.example.mapper.ArticleMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {
    
    @Resource
    private ArticleMapper articleMapper;

    public void add(Article article) {
        article.setDate(DateUtil.now());
        article.setReadCount(0);
        articleMapper.insert(article);
    }

    public void updateById(Article article) {
        articleMapper.updateById(article);
    }

    public void deleteById(Integer id) {
        articleMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            articleMapper.deleteById(id);
        }
    }

    public Article selectById(Integer id) {
        return articleMapper.selectById(id);
    }

    public List<Article> selectAll(Article article) {
        return articleMapper.selectAll(article);
    }

    public PageInfo<Article> selectPage(Article article, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Article> list = articleMapper.selectAll(article);
        return PageInfo.of(list);
    }

    public List<Article> selectRecommend() {
        return articleMapper.selectRecommend();
    }
}
