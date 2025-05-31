package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Car;
import com.example.mapper.CarMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Business-level methods
 */
@Service
public class CarService {

    @Resource
    private CarMapper carMapper;

    public void add(Car car) {
        car.setDate(DateUtil.now());
        carMapper.insert(car);
    }

    public void updateById(Car car) {
        carMapper.updateById(car);
    }

    public void deleteById(Integer id) {
        carMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            carMapper.deleteById(id);
        }
    }

    public Car selectById(Integer id) {
        return carMapper.selectById(id);
    }

    public List<Car> selectAll(Car car) {
        return carMapper.selectAll(car);
    }

    public PageInfo<Car> selectPage(Car car, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Car> list = carMapper.selectAll(car);
        return PageInfo.of(list);
    }

}
