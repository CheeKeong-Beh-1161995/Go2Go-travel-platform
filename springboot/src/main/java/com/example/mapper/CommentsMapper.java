package com.example.mapper;

import com.example.entity.Comments;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CommentsMapper {

    int insert(Comments comments);

    void updateById(Comments comments);

    void deleteById(Integer id);

    @Select("select * from `comments` where id = #{id}")
    Comments selectById(Integer id);

    List<Comments> selectAll(Comments comments);

}
