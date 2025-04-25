package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Travels;
import com.example.mapper.TravelsMapper;
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
public class TravelsService {

    @Resource
    private TravelsMapper travelsMapper;

    public void add(Travels travels) {
        travels.setTime(DateUtil.now());
        travelsMapper.insert(travels);
    }

    public void updateById(Travels travels) {
        travelsMapper.updateById(travels);
    }

    public void deleteById(Integer id) {
        travelsMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            travelsMapper.deleteById(id);
        }
    }

    public Travels selectById(Integer id) {
        return travelsMapper.selectById(id);
    }

    public void updateReadCount(Integer id) {
        travelsMapper.updateReadCount(id);
    }

    public List<Travels> selectAll(Travels travels) {
        return travelsMapper.selectAll(travels);
    }

    public PageInfo<Travels> selectPage(Travels travels, Integer pageNum, Integer pageSize) {
        Account account = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(account.getRole())) {
            travels.setStatus("通过");
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Travels> list = travelsMapper.selectAll(travels);
        return PageInfo.of(list);
    }

}
