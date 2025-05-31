package com.example.controller;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.Comments;
import com.example.service.CommentsService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Frontend request interface
 */
@RestController
@RequestMapping("/comment")
public class CommentsController {

    @Resource
    private CommentsService commentsService;

    /**
     * Newly added
     */
    @PostMapping("/add")
    public Result add(@RequestBody Comments comments) {
        commentsService.add(comments);
        return Result.success();
    }

    /**
     * Modify
     */
    @PutMapping("/update")
    public Result update(@RequestBody Comments comments) {
        commentsService.updateById(comments);
        return Result.success();
    }

    /**
     * Delete individually
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        commentsService.deleteById(id);
        return Result.success();
    }

    /**
     * Batch delete
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        commentsService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * Single query
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Comments comments = commentsService.selectById(id);
        return Result.success(comments);
    }

    /**
     * Query all
     */
    @GetMapping("/selectAll")
    public Result selectAll(Comments comments) {
        List<Comments> list = commentsService.selectAll(comments);
        return Result.success(list);
    }

    /**
     * Paged Query
     */
    @GetMapping("/selectPage")
    public Result selectPage(Comments comments,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Comments> pageInfo = commentsService.selectPage(comments, pageNum, pageSize);
        return Result.success(pageInfo);
    }

    @GetMapping("/selectTree")
    public Result selectTree(Comments comments,
                             @RequestParam(defaultValue = "1")Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Comments> pageInfo = commentsService.selectTree(comments, pageNum, pageSize);
        return Result.success(pageInfo);
    }

    @GetMapping("/selectCount/{fid}/{module}")
    public Result selectCount(@PathVariable Integer fid ,@PathVariable String module) {
        Integer count = commentsService.selectCount(fid,module);
        return Result.success(count);
    }
    /**
     * Export data in batches
     */
    @GetMapping("/export")
    public void exportData(HttpServletResponse response) throws IOException {
        ExcelWriter excelWriter = ExcelUtil.getWriter(true);
        List<Comments> list = commentsService.selectAll(null);
        excelWriter.write(list);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("TourismInformation", "UTF-8") + ".xlsx");
        ServletOutputStream out = response.getOutputStream();
        excelWriter.flush(out,true);
        out.flush();
        excelWriter.close();
        out.close();
    }
}
