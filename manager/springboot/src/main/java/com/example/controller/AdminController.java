package com.example.controller;

import com.example.common.Result;
import com.example.entity.Admin;
import com.example.service.AdminService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/**
 * 管理员前端操作接口
 **/
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminService adminService; // 注入 AdminService 服务，用于处理管理员相关操作

    /**
     * 新增管理员
     */
    @PostMapping("/add")
    public Result add(@RequestBody Admin admin) {
        adminService.add(admin); // 调用服务层方法添加管理员
        return Result.success(); // 返回成功结果
    }

    /**
     * 删除管理员
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        adminService.deleteById(id); // 调用服务层方法根据 ID 删除管理员
        return Result.success(); // 返回成功结果
    }

    /**
     * 批量删除管理员
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        adminService.deleteBatch(ids); // 调用服务层方法批量删除管理员
        return Result.success(); // 返回成功结果
    }

    /**
     * 修改管理员信息
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Admin admin) {
        adminService.updateById(admin); // 调用服务层方法更新管理员信息
        return Result.success(); // 返回成功结果
    }

    /**
     * 根据ID查询管理员
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Admin admin = adminService.selectById(id); // 调用服务层方法根据 ID 查询管理员
        return Result.success(admin); // 返回查询结果
    }

    /**
     * 查询所有管理员
     */
    @GetMapping("/selectAll")
    public Result selectAll(Admin admin) {
        List<Admin> list = adminService.selectAll(admin); // 调用服务层方法查询所有管理员
        return Result.success(list); // 返回查询结果
    }

    /**
     * 分页查询管理员
     */
    @GetMapping("/selectPage")
    public Result selectPage(Admin admin,
                             @RequestParam(defaultValue = "1") Integer pageNum,  // 默认页码为1
                             @RequestParam(defaultValue = "10") Integer pageSize) { // 默认每页大小为10
        PageInfo<Admin> page = adminService.selectPage(admin, pageNum, pageSize); // 调用服务层方法分页查询管理员
        return Result.success(page); // 返回分页查询结果
    }

}
