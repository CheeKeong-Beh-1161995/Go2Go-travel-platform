package com.example.mapper;

import com.example.entity.Review;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface ReviewMapper {

    int insert(Review review);

    void updateById(Review review);

    void deleteById(Integer id);

    @Select("select review.*, user.name as userName from `review` left join user on review.user_id = user.id where review.id = #{id}")
    Review selectById(Integer id);


    List<Review> selectAll(Review review);

}
