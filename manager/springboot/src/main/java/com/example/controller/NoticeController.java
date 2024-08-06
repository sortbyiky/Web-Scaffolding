package com.example.controller;

import com.example.common.Result;
import com.example.entity.Notice;
import com.example.service.NoticeService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/**
 * 公告信息表前端操作接口
 **/
@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Resource
    private NoticeService noticeService; // 注入 NoticeService 服务，用于处理公告相关操作

    /**
     * 新增公告
     */
    @PostMapping("/add")
    public Result add(@RequestBody Notice notice) {
        noticeService.add(notice); // 调用服务层方法添加公告
        return Result.success(); // 返回成功结果
    }

    /**
     * 删除公告
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        noticeService.deleteById(id); // 调用服务层方法根据 ID 删除公告
        return Result.success(); // 返回成功结果
    }

    /**
     * 批量删除公告
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        noticeService.deleteBatch(ids); // 调用服务层方法批量删除公告
        return Result.success(); // 返回成功结果
    }

    /**
     * 修改公告信息
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Notice notice) {
        noticeService.updateById(notice); // 调用服务层方法更新公告信息
        return Result.success(); // 返回成功结果
    }

    /**
     * 根据ID查询公告
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Notice notice = noticeService.selectById(id); // 调用服务层方法根据 ID 查询公告
        return Result.success(notice); // 返回查询结果
    }

    /**
     * 查询所有公告
     */
    @GetMapping("/selectAll")
    public Result selectAll(Notice notice) {
        List<Notice> list = noticeService.selectAll(notice); // 调用服务层方法查询所有公告
        return Result.success(list); // 返回查询结果
    }

    /**
     * 分页查询公告
     */
    @GetMapping("/selectPage")
    public Result selectPage(Notice notice,
                             @RequestParam(defaultValue = "1") Integer pageNum,  // 默认页码为1
                             @RequestParam(defaultValue = "10") Integer pageSize) { // 默认每页大小为10
        PageInfo<Notice> page = noticeService.selectPage(notice, pageNum, pageSize); // 调用服务层方法分页查询公告
        return Result.success(page); // 返回分页查询结果
    }

}
