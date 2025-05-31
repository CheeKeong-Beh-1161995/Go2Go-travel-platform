package com.example.controller;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.Review;
import com.example.service.ReviewService;
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
@RequestMapping("/review")
public class ReviewController {

    @Resource
    private ReviewService reviewService;

    /**
     * Newly added
     */
    @PostMapping("/add")
    public Result add(@RequestBody Review review) {
        reviewService.add(review);
        return Result.success();
    }

    /**
     * Modify
     */
    @PutMapping("/update")
    public Result update(@RequestBody Review review) {
        reviewService.updateById(review);
        return Result.success();
    }

    /**
     * Delete individually
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        reviewService.deleteById(id);
        return Result.success();
    }

    /**
     * Batch delete
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        reviewService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * Single query
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Review review = reviewService.selectById(id);
        return Result.success(review);
    }
    /**
     * Query all
     */
    @GetMapping("/selectAll")
    public Result selectAll(Review review) {
        List<Review> list = reviewService.selectAll(review);
        return Result.success(list);
    }

    /**
     * Paged Query
     */
    @GetMapping("/selectPage")
    public Result selectPage(Review review,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Review> pageInfo = reviewService.selectPage(review, pageNum, pageSize);
        return Result.success(pageInfo);
    }

    /**
     * Export data in batches
     */
    @GetMapping("/export")
    public void exportData(HttpServletResponse response) throws IOException {
        ExcelWriter excelWriter = ExcelUtil.getWriter(true);
        List<Review> list = reviewService.selectAll(null);
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
