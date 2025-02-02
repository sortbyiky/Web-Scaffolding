package com.example.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.common.Constants;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Token工具类
 */
@Component
public class TokenUtils {

    private static final Logger log = LoggerFactory.getLogger(TokenUtils.class); // 初始化日志对象，用于记录日志信息

    private static AdminService staticAdminService; // 静态变量，用于存储 AdminService 实例

    @Resource
    AdminService adminService; // 注入 AdminService，用于获取管理员信息

    // 使用 @PostConstruct 注解的方法会在依赖注入完成后被自动调用
    @PostConstruct
    public void setUserService() {
        staticAdminService = adminService; // 将实例化的 AdminService 赋值给静态变量
    }

    /**
     * 生成token
     */
    public static String createToken(String data, String sign) {
        return JWT.create().withAudience(data) // 将 userId-role 保存到 token 里面，作为载荷
                .withExpiresAt(DateUtil.offsetHour(new Date(), 2)) // 设置 token 2小时后过期
                .sign(Algorithm.HMAC256(sign)); // 以 password 作为 token 的密钥进行签名
    }

    /**
     * 获取当前登录的用户信息
     */
    public static Account getCurrentUser() {
        try {
            // 获取当前 HTTP 请求对象
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            // 从请求头中获取 token
            String token = request.getHeader(Constants.TOKEN);
            if (ObjectUtil.isNotEmpty(token)) {
                // 解析 token 中的受众信息
                String userRole = JWT.decode(token).getAudience().get(0);
                String userId = userRole.split("-")[0];  // 获取用户ID
                String role = userRole.split("-")[1];    // 获取角色
                // 根据角色判断是否为管理员
                if (RoleEnum.ADMIN.name().equals(role)) {
                    // 使用静态 AdminService 实例查询管理员信息
                    return staticAdminService.selectById(Integer.valueOf(userId));
                }
            }
        } catch (Exception e) {
            // 记录获取用户信息时发生的异常
            log.error("获取当前用户信息出错", e);
        }
        return new Account();  // 如果未找到用户，返回空的账号对象
    }
}
