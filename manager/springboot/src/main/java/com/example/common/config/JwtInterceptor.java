package com.example.common.config;

import cn.hutool.core.util.ObjectUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.common.Constants;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.exception.CustomException;
import com.example.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT 拦截器
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(JwtInterceptor.class);

    @Resource
    private AdminService adminService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 1. 从 HTTP 请求的 header 中获取 token
        String token = request.getHeader(Constants.TOKEN);
        if (ObjectUtil.isEmpty(token)) {
            // 如果 header 中没有 token，从请求参数中获取 token
            token = request.getParameter(Constants.TOKEN);
        }
        // 2. 开始执行认证
        if (ObjectUtil.isEmpty(token)) {
            // 如果 token 为空，抛出 TOKEN_INVALID_ERROR 异常
            throw new CustomException(ResultCodeEnum.TOKEN_INVALID_ERROR);
        }
        Account account = null;
        try {
            // 解析 token 获取存储的数据
            String userRole = JWT.decode(token).getAudience().get(0); // 获取 token 中的受众信息
            String userId = userRole.split("-")[0]; // 提取用户 ID
            String role = userRole.split("-")[1]; // 提取角色信息
            // 根据 userId 查询数据库
            if (RoleEnum.ADMIN.name().equals(role)) { // 判断角色是否为 ADMIN
                account = adminService.selectById(Integer.valueOf(userId)); // 根据 userId 获取管理员账户信息
            }
        } catch (Exception e) {
            // 如果解析 token 失败，抛出 TOKEN_CHECK_ERROR 异常
            throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
        }
        if (ObjectUtil.isNull(account)) {
            // 如果用户不存在，抛出 USER_NOT_EXIST_ERROR 异常
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        try {
            // 使用用户密码创建 JWT 验证器，验证 token
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(account.getPassword())).build();
            jwtVerifier.verify(token); // 验证 token
        } catch (JWTVerificationException e) {
            // 如果验证失败，抛出 TOKEN_CHECK_ERROR 异常
            throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
        }
        return true; // 验证通过，允许请求继续执行
    }
}
