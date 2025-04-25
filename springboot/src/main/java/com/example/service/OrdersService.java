package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Car;
import com.example.entity.Orders;
import com.example.entity.Tourism;
import com.example.exception.CustomException;
import com.example.mapper.CarMapper;
import com.example.mapper.OrdersMapper;
import com.example.mapper.TourismMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 业务层方法
 */
@Service
public class OrdersService {

    @Resource
    private OrdersMapper ordersMapper;
    @Resource
    private TourismMapper tourismMapper;
    @Resource
    private CarMapper carMapper;

    @Transactional
    public void add(Orders orders) {
        Account currentUser = TokenUtils.getCurrentUser();
        orders.setUserId(currentUser.getId());
        orders.setStatus("待支付");
        orders.setOrderNo(IdUtil.getSnowflakeNextIdStr());
        Integer tourismId = orders.getTourismId();
        Tourism tourism = tourismMapper.selectById(tourismId);
        Car car = carMapper.selectById(tourismId);
        //在这里加   比如
        //tourism.setPrice(tourism.getPrice().multiply(new BigDecimal("0.5")));
        if (tourism == null && car == null) {
            throw new CustomException("500","商品不存在");
        }
        if (tourism == null) {
            if (car.getStore() < orders.getNum()) {
                throw new CustomException("500","库存不足");
            }
        }
        if (car == null) {
            if (tourism.getStore() < orders.getNum()) {
                throw new CustomException("500","库存不足");
            }
        }
        orders.setTime(DateUtil.now());
        if (car == null) {
            orders.setTourismPrice(tourism.getPrice());
            ordersMapper.insert(orders);
            tourism.setStore(tourism.getStore() - orders.getNum());
            tourismMapper.updateById(tourism);
        } else {
            orders.setTourismPrice(car.getPrice());
            ordersMapper.insert(orders);
            car.setStore(car.getStore() - orders.getNum());
            carMapper.updateById(car);
        }
    }

    public void updateById(Orders orders) {
        ordersMapper.updateById(orders);
    }

    public void deleteById(Integer id) {
        ordersMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            ordersMapper.deleteById(id);
        }
    }

    public Orders selectById(Integer id) {
        return ordersMapper.selectById(id);
    }

    public List<Orders> selectAll(Orders orders) {
        return ordersMapper.selectAll(orders);
    }

    public PageInfo<Orders> selectPage(Orders orders, Integer pageNum, Integer pageSize) {
        Account account = TokenUtils.getCurrentUser();
        if (account.getRole().equals(RoleEnum.USER.name())) {
            orders.setUserId(account.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Orders> list = ordersMapper.selectAll(orders);
        return PageInfo.of(list);
    }

}
