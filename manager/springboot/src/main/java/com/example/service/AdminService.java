package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.Constants;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.exception.CustomException;
import com.example.mapper.AdminMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 管理员业务处理
 **/
@Service
public class AdminService {

    @Resource
    private AdminMapper adminMapper; // 注入 AdminMapper，用于执行数据库操作

    /**
     * 新增管理员
     */
    public void add(Admin admin) {
        // 检查用户名是否已存在
        Admin dbAdmin = adminMapper.selectByUsername(admin.getUsername());
        if (ObjectUtil.isNotNull(dbAdmin)) {
            // 如果用户名已存在，抛出异常
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }
        // 如果密码为空，设置为默认密码
        if (ObjectUtil.isEmpty(admin.getPassword())) {
            admin.setPassword(Constants.USER_DEFAULT_PASSWORD);
        }
        // 如果姓名为空，设置为用户名
        if (ObjectUtil.isEmpty(admin.getName())) {
            admin.setName(admin.getUsername());
        }
        // 设置角色为管理员
        admin.setRole(RoleEnum.ADMIN.name());
        // 插入管理员记录到数据库
        adminMapper.insert(admin);
    }

    /**
     * 删除管理员
     */
    public void deleteById(Integer id) {
        // 根据 ID 删除管理员
        adminMapper.deleteById(id);
    }

    /**
     * 批量删除管理员
     */
    public void deleteBatch(List<Integer> ids) {
        // 遍历 ID 列表，逐个删除管理员
        for (Integer id : ids) {
            adminMapper.deleteById(id);
        }
    }

    /**
     * 修改管理员信息
     */
    public void updateById(Admin admin) {
        // 更新管理员信息
        adminMapper.updateById(admin);
    }

    /**
     * 根据ID查询管理员
     */
    public Admin selectById(Integer id) {
        // 根据 ID 查询管理员信息
        return adminMapper.selectById(id);
    }

    /**
     * 查询所有管理员
     */
    public List<Admin> selectAll(Admin admin) {
        // 查询所有管理员信息
        return adminMapper.selectAll(admin);
    }

    /**
     * 分页查询管理员
     */
    public PageInfo<Admin> selectPage(Admin admin, Integer pageNum, Integer pageSize) {
        // 设置分页信息
        PageHelper.startPage(pageNum, pageSize);
        // 查询管理员列表
        List<Admin> list = adminMapper.selectAll(admin);
        // 返回分页结果
        return PageInfo.of(list);
    }

    /**
     * 管理员登录
     */
    public Account login(Account account) {
        // 根据用户名查询管理员信息
        Account dbAdmin = adminMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbAdmin)) {
            // 如果管理员不存在，抛出异常
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbAdmin.getPassword())) {
            // 如果密码不正确，抛出异常
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
        }
        // 生成 token
        String tokenData = dbAdmin.getId() + "-" + RoleEnum.ADMIN.name();
        String token = TokenUtils.createToken(tokenData, dbAdmin.getPassword());
        // 设置 token 到管理员对象中
        dbAdmin.setToken(token);
        return dbAdmin;
    }

    /**
     * 注册新管理员
     */
    public void register(Account account) {
        // 创建新的管理员对象
        Admin admin = new Admin();
        // 复制账户信息到管理员对象
        BeanUtils.copyProperties(account, admin);
        // 调用新增方法
        add(admin);
    }

    /**
     * 修改管理员密码
     */
    public void updatePassword(Account account) {
        // 根据用户名查询管理员信息
        Admin dbAdmin = adminMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbAdmin)) {
            // 如果管理员不存在，抛出异常
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbAdmin.getPassword())) {
            // 如果当前密码不正确，抛出异常
            throw new CustomException(ResultCodeEnum.PARAM_PASSWORD_ERROR);
        }
        // 更新密码
        dbAdmin.setPassword(account.getNewPassword());
        // 更新数据库中的管理员信息
        adminMapper.updateById(dbAdmin);
    }

}
