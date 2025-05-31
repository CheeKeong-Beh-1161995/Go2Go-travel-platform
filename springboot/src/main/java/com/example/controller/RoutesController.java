package com.example.controller;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.Routes;
import com.example.service.RoutesService;
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
@RequestMapping("/routes")
public class RoutesController {

    @Resource
    private RoutesService routesService;

    /**
     * Newly added
     */
    @PostMapping("/add")
    public Result add(@RequestBody Routes routes) {
        routesService.add(routes);
        return Result.success();
    }

    /**
     * Modify
     */
    @PutMapping("/update")
    public Result update(@RequestBody Routes routes) {
        routesService.updateById(routes);
        return Result.success();
    }

    /**
     * Delete individually
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        routesService.deleteById(id);
        return Result.success();
    }

    /**
     * Batch delete
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        routesService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * Single query
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Routes routes = routesService.selectById(id);
        return Result.success(routes);
    }

    /**
     * Query all
     */
    @GetMapping("/selectAll")
    public Result selectAll(Routes routes) {
        List<Routes> list = routesService.selectAll(routes);
        return Result.success(list);
    }

    /**
     * Paged Query
     */
    @GetMapping("/selectPage")
    public Result selectPage(Routes routes,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Routes> pageInfo = routesService.selectPage(routes, pageNum, pageSize);
        return Result.success(pageInfo);
    }

    /**
     * Export data in batches
     */
    @GetMapping("/export")
    public void exportData(HttpServletResponse response) throws IOException {
        ExcelWriter excelWriter = ExcelUtil.getWriter(true);
        List<Routes> list = routesService.selectAll(null);
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
