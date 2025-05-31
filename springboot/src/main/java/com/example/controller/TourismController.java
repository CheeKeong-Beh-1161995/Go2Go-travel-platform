package com.example.controller;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.Car;
import com.example.entity.Tourism;
import com.example.service.TourismService;
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
@RequestMapping("/tourism")
public class TourismController {

    @Resource
    private TourismService tourismService;

    /**
     * Newly added
     */
    @PostMapping("/add")
    public Result add(@RequestBody Tourism tourism) {
        tourismService.add(tourism);
        return Result.success();
    }

    /**
     * Modify
     */
    @PutMapping("/update")
    public Result update(@RequestBody Tourism tourism) {
        tourismService.updateById(tourism);
        return Result.success();
    }

    /**
     * Single query
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        tourismService.deleteById(id);
        return Result.success();
    }

    /**
     * Batch delete
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        tourismService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * Single query
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Tourism tourism = tourismService.selectById(id);
        return Result.success(tourism);
    }

    /**
     * Query all
     */
    @GetMapping("/selectAll")
    public Result selectAll(Tourism tourism) {
        List<Tourism> list = tourismService.selectAll(tourism);
        return Result.success(list);
    }

    /**
     * Paged Query
     */
    @GetMapping("/selectPage")
    public Result selectPage(Tourism tourism,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Tourism> pageInfo = tourismService.selectPage(tourism, pageNum, pageSize);
        return Result.success(pageInfo);
    }

    /**
     * Export data in batches
     */
    @GetMapping("/export")
    public void exportData(HttpServletResponse response) throws IOException {
        ExcelWriter excelWriter = ExcelUtil.getWriter(true);
        List<Tourism> tourismList = tourismService.selectAll(null);
        excelWriter.write(tourismList);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("TourismInformation", "UTF-8") + ".xlsx");
        ServletOutputStream out = response.getOutputStream();
        excelWriter.flush(out,true);
        out.flush();
        excelWriter.close();
        out.close();
    }

}
