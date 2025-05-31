package com.example.mapper;

import com.example.entity.Comments;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CommentsMapper {

    int insert(Comments comments);

    void updateById(Comments comments);

    void deleteById(Integer id);

    @Select("select * from `comment` where id = #{id}")
    Comments selectById(Integer id);

    @Select("select count(*) from `comment` where fid = #{fid} and module = #{module}")
    Integer selectCount(@Param("fid") Integer fid,@Param("module") String module);

    List<Comments> selectRoot(Comments comments);

    List<Comments> selectByRootId(Integer id);

    List<Comments> selectAll(Comments comments);

}
