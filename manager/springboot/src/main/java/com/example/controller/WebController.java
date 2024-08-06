package com.example.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.example.common.Result;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.service.AdminService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 基础前端接口
 */
@RestController
public class WebController {

    @Resource
    private AdminService adminService; // 注入 AdminService 服务，用于处理管理员相关操作

    // 首页接口，简单返回成功信息
    @GetMapping("/")
    public Result hello() {
        return Result.success("访问成功"); // 返回成功信息
    }

    /**
     * 登录接口
     */
    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        // 检查账户信息是否完整（用户名、密码、角色）
        if (ObjectUtil.isEmpty(account.getUsername()) || ObjectUtil.isEmpty(account.getPassword())
                || ObjectUtil.isEmpty(account.getRole())) {
            return Result.error(ResultCodeEnum.PARAM_LOST_ERROR); // 返回参数缺失错误
        }
        // 判断角色是否为管理员
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            account = adminService.login(account); // 调用服务层的登录方法
        }
        return Result.success(account); // 返回登录结果
    }

    /**
     * 注册接口
     */
    @PostMapping("/register")
    public Result register(@RequestBody Account account) {
        // 检查账户信息是否完整（用户名、密码、角色）
        if (StrUtil.isBlank(account.getUsername()) || StrUtil.isBlank(account.getPassword())
                || ObjectUtil.isEmpty(account.getRole())) {
            return Result.error(ResultCodeEnum.PARAM_LOST_ERROR); // 返回参数缺失错误
        }
        // 判断角色是否为管理员
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            adminService.register(account); // 调用服务层的注册方法
        }
        return Result.success(); // 返回成功结果
    }

    /**
     * 修改密码接口
     */
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account) {
        // 检查账户信息是否完整（用户名、当前密码、新密码）
        if (StrUtil.isBlank(account.getUsername()) || StrUtil.isBlank(account.getPassword())
                || ObjectUtil.isEmpty(account.getNewPassword())) {
            return Result.error(ResultCodeEnum.PARAM_LOST_ERROR); // 返回参数缺失错误
        }
        // 判断角色是否为管理员
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            adminService.updatePassword(account); // 调用服务层的修改密码方法
        }
        return Result.success(); // 返回成功结果
    }

}
