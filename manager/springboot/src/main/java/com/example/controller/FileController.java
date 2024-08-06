package com.example.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.example.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * 文件接口
 */
@RestController
@RequestMapping("/files")
public class FileController {

    // 文件上传存储路径
    // System.getProperty("user.dir")：获取当前应用程序的工作目录。这通常是运行应用程序时所在的目录。
    private static final String filePath = System.getProperty("user.dir") + "/files/";

    // 服务器端口，默认为9090
    @Value("${server.port:9090}")
    private String port;

    // IP地址，默认为localhost
    @Value("${ip:localhost}")
    private String ip;

    /**
     * 文件上传方法
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile file) {
        String flag;
        // 同步代码块，用于生成文件的唯一标识
        synchronized (FileController.class) {
            flag = System.currentTimeMillis() + ""; // 使用当前时间戳作为标识
            ThreadUtil.sleep(1L); // 休眠1毫秒，确保唯一性
        }
        String fileName = file.getOriginalFilename(); // 获取上传文件的原始名称
        try {
            if (!FileUtil.isDirectory(filePath)) { // 检查文件存储路径是否存在
                FileUtil.mkdir(filePath); // 如果不存在，则创建路径
            }
            // 文件存储形式：时间戳-文件名
            FileUtil.writeBytes(file.getBytes(), filePath + flag + "-" + fileName);  // 保存文件到指定路径
            System.out.println(fileName + "--上传成功");

        } catch (Exception e) {
            System.err.println(fileName + "--文件上传失败"); // 异常处理，打印错误信息
        }
        // 返回文件的访问URL
        String http = "http://" + ip + ":" + port + "/files/";
        return Result.success(http + flag + "-" + fileName);  // 返回文件的访问路径
    }

    /**
     * 获取文件
     *
     * @param flag 文件的唯一标识
     * @param response HTTP响应
     */
    @GetMapping("/{flag}")   // 文件路径标识，例如：1697438073596-avatar.png
    public void avatarPath(@PathVariable String flag, HttpServletResponse response) {
        OutputStream os;
        try {
            if (StrUtil.isNotEmpty(flag)) { // 检查标识是否不为空
                // 设置响应头，告知浏览器以附件形式下载
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(flag, "UTF-8"));
                response.setContentType("application/octet-stream"); // 设置响应内容类型
                byte[] bytes = FileUtil.readBytes(filePath + flag); // 读取文件字节
                os = response.getOutputStream(); // 获取响应输出流
                os.write(bytes); // 将文件字节写入输出流
                os.flush(); // 刷新输出流
                os.close(); // 关闭输出流
            }
        } catch (Exception e) {
            System.out.println("文件下载失败"); // 异常处理，打印错误信息
        }
    }

    /**
     * 删除文件
     *
     * @param flag 文件的唯一标识
     */
    @DeleteMapping("/{flag}")
    public void delFile(@PathVariable String flag) {
        FileUtil.del(filePath + flag); // 删除指定文件
        System.out.println("删除文件" + flag + "成功"); // 打印删除成功信息
    }
}
