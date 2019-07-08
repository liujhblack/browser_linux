package com.liujhblack.controller;/**
 * Created by liujunhui on 2019/7/2.
 */

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;
import com.liujhblack.domain.SSHInfo;
import com.liujhblack.util.Result;
import com.liujhblack.util.SSHMapUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * @Author liujunhui
 * @Date 2019/7/2 16:34
 * @Description TODO
 */
@RestController
public class SSHInfoController {

    /**
     * @Author liujunhui
     * @Date 2019/7/8 14:36
     * @Description 连接linux
     */
    @PostMapping("/sshInfo")
    public Result save(SSHInfo sshInfo) {
        try {
            Connection connection = new Connection(sshInfo.getHostname(), sshInfo.getPort());
            connection.connect();
            boolean flag = connection.authenticateWithPassword(sshInfo.getUsername(), sshInfo.getPassword());
            if (!flag) {
                return Result.isError("连接linux失败");
            }
            Session session = connection.openSession();
            session.requestPTY("bash");
            session.startShell();

            String randomString = UUID.randomUUID().toString().replace("-", "");
            SSHMapUtil.addSSHConnection(randomString, connection);
            SSHMapUtil.addSSHSession(randomString, session);

            return Result.isSuccess(randomString);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.isError("连接linux失败");
        }
    }

    /**
     * @Author liujunhui
     * @Date 2019/7/8 14:37
     * @Description 文件下载 从远程linux到浏览器
     */
    @PostMapping("/download")
    public Result download(String path, String randomString, HttpServletResponse response) throws IOException {
        ServletOutputStream outputStream = response.getOutputStream();
        Session sshSession = SSHMapUtil.getSSHSession(randomString);
        Connection sshConnection = SSHMapUtil.getSSHConnection(randomString);
        if (sshConnection == null) {
            return Result.isError("连接已断开");
        }
        SCPClient scpClient;
        try {
            scpClient = sshConnection.createSCPClient();
        } catch (IOException e) {
            e.printStackTrace();
            return Result.isError("连接创建失败");
        }
        String filename = path.substring(path.lastIndexOf("/") + 1);

        //是文件直接下载
        if (filename.contains(".")) {
            response.setHeader("content-type", "application/octet-stream");
            response.addHeader("Content-Disposition", "attachment;fileName=" + filename);
            try {
                scpClient.get(path, outputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {//是文件夹，打包下载
            return Result.isError("不支持文件夹下载");
        }
        return null;
    }

    /**
     * @Author liujunhui
     * @Date 2019/7/8 14:37
     * @Description 文件上传 从浏览器上传文件到linux
     */
    @PostMapping("upload")
    public Result upload(@RequestParam MultipartFile file, String path, String randomString) {
        Connection sshConnection = SSHMapUtil.getSSHConnection(randomString);
        if (sshConnection == null) {
            return Result.isError("连接已断开");
        }
        SCPClient scpClient;
        try {
            scpClient = sshConnection.createSCPClient();
        } catch (IOException e) {
            e.printStackTrace();
            return Result.isError("连接创建失败");
        }
        try {
            scpClient.put(file.getBytes(), file.getOriginalFilename(), path);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.isError("文件传输失败，请检查目录是否存在");
        }
        return Result.isSuccess();
    }

    @GetMapping("/sshStatus")
    public Result getSSHStatus(String randomString){
        Connection sshConnection = SSHMapUtil.getSSHConnection(randomString);
        if (sshConnection == null) {
            return Result.isSuccess("0");//表示连接已断开
        }else{
            return Result.isSuccess("1");//表示连接未断开
        }
    }

}
