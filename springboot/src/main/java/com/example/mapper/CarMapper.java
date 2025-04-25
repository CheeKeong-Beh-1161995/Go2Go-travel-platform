package com.example.mapper;

import com.example.entity.Car;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CarMapper {

    int insert(Car car);

    void updateById(Car car);

    void deleteById(Integer id);

    @Select("select * from `car` where id = #{id}")
    Car selectById(Integer id);

    List<Car> selectAll(Car car);

}
