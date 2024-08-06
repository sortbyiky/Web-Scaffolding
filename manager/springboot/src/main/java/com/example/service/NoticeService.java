package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Account;
import com.example.entity.Notice;
import com.example.mapper.NoticeMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 公告信息表业务处理
 **/
@Service
public class NoticeService {

    @Resource
    private NoticeMapper noticeMapper; // 注入 NoticeMapper，用于执行数据库操作

    /**
     * 新增公告
     */
    public void add(Notice notice) {
        // 设置公告的发布日期为今天
        notice.setTime(DateUtil.today());
        // 获取当前用户信息
        Account currentUser = TokenUtils.getCurrentUser();
        // 设置公告发布者为当前用户
        notice.setUser(currentUser.getUsername());
        // 插入公告记录到数据库
        noticeMapper.insert(notice);
    }

    /**
     * 删除公告
     */
    public void deleteById(Integer id) {
        // 根据 ID 删除公告
        noticeMapper.deleteById(id);
    }

    /**
     * 批量删除公告
     */
    public void deleteBatch(List<Integer> ids) {
        // 遍历 ID 列表，逐个删除公告
        for (Integer id : ids) {
            noticeMapper.deleteById(id);
        }
    }

    /**
     * 修改公告信息
     */
    public void updateById(Notice notice) {
        // 更新公告信息
        noticeMapper.updateById(notice);
    }

    /**
     * 根据ID查询公告
     */
    public Notice selectById(Integer id) {
        // 根据 ID 查询公告信息
        return noticeMapper.selectById(id);
    }

    /**
     * 查询所有公告
     */
    public List<Notice> selectAll(Notice notice) {
        // 查询所有公告信息
        return noticeMapper.selectAll(notice);
    }

    /**
     * 分页查询公告
     */
    public PageInfo<Notice> selectPage(Notice notice, Integer pageNum, Integer pageSize) {
        // 设置分页信息
        PageHelper.startPage(pageNum, pageSize);
        // 查询公告列表
        List<Notice> list = noticeMapper.selectAll(notice);
        // 返回分页结果
        return PageInfo.of(list);
    }

}
