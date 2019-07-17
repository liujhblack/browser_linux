package com.liujhblack.controller;/**
 * Created by liujunhui on 2019/7/2.
 */

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;
import com.liujhblack.domain.SSHInfo;
import com.liujhblack.util.EncryptUtil;
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
    public Result save(String encrypted) {
        try {
            //解密 RSA非对称加密算法
            String rsaPrivateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANl/EhdHlblObDgF757MzSMfunwpGuelDJvTrv3BgTEBehNcT/WQtNrP/734yVvE2E2ThH8DwV26TSgdwvTfbxIw5lVQABiEkKjElh4sFd99YlHUCSWMG/aabYU5ZBN4LICZ/VrbJYXfDR/T6ZuDau/Jx7ELqJTuqIx+qwcV2p/7AgMBAAECgYEAt3/zRnraAr78pQO1GHjYNmMllm2jyn7BNZOSl3u0QSFq2nzO5XNScy58Kc6GLIvWpxTn+7WyZh6xzD/X5XvBm7xCt+L6ZPt5O7/wqUqjp6WAPufymbXwBiyyzz0DJL6w+0CkMul6FivjV628zHVgSYi7jlkRTk9W9m7I2S5YusECQQDtkcxlvNrc38o/tvxWyW5sY8O9rmHIZdGVMyt0bcYBlsRrg2M6mBWtZfWUfNRpeSqBHft+q3v5ezO1VMkYO2hhAkEA6l6dB1ilHD9E+Q1LrmmYPmQN1FgP+9YNoH22CgKpLpKwWpxqaaRhhaCccrGqMjmFM5nnA3R8ifF12qIARNR12wJBAMQagBDTLg75JGgn0nCJYf9S8vcWhVz4v2JblNlM7A/Ptl/RWw25ENvLuEZULLrL7AwdBcbwIywzSOG8FStNjsECQDJ7Fo+ShF3FMvIB7x8uF2C45FGsdiTkQiMjcKZPVGl3pwydTD5c7bR+l7QMmIAg65PlvmB8IqcDn0LsSeqJaKkCQCEOF5Zk4nqXD0363LbgucRuJpn4sES/6uJL0jCDu1AGxZq2wvbl6sJ1EG5/cHVgzXc9NmWBcvFLN7q9Oq6pGXg=";
            String res = EncryptUtil.rsaPrivateKeyDecode(encrypted, rsaPrivateKey);

            String[] split = res.split(",");
            SSHInfo sshInfo = new SSHInfo();
            sshInfo.setHostname(split[0]);
            sshInfo.setUsername(split[1]);
            sshInfo.setPassword(split[2]);
            sshInfo.setPort(Integer.valueOf(split[3]));

            Connection connection = new Connection(sshInfo.getHostname(), sshInfo.getPort());
            connection.connect();
            boolean flag = connection.authenticateWithPassword(sshInfo.getUsername(), sshInfo.getPassword());
            if (!flag) {
                return Result.isError("连接linux失败");
            }
            Session session = connection.openSession();
//            session.requestPTY("bash");base会导致top，vi命令不可用xterm
            session.requestPTY("xterm",90,30,0,0,null);
            session.startShell();

            String randomString = UUID.randomUUID().toString().replace("-", "");
            SSHMapUtil.addSSHConnection(randomString, connection);
            SSHMapUtil.addSSHSession(randomString, session);
            SSHMapUtil.addSSHInfo(randomString,sshInfo);

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
        SSHInfo sshInfo = SSHMapUtil.getSSHInfo(randomString);
        Connection sshConnection = new Connection(sshInfo.getHostname(), sshInfo.getPort());
        sshConnection.connect();
        boolean flag = sshConnection.authenticateWithPassword(sshInfo.getUsername(), sshInfo.getPassword());
        if (!flag) {
            return Result.isError("连接linux失败");
        }
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
        } else {//是文件夹，不支持
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
    public Result upload(@RequestParam MultipartFile file, String path, String randomString) throws IOException {
        SSHInfo sshInfo = SSHMapUtil.getSSHInfo(randomString);
        Connection sshConnection = new Connection(sshInfo.getHostname(), sshInfo.getPort());
        sshConnection.connect();
        boolean flag = sshConnection.authenticateWithPassword(sshInfo.getUsername(), sshInfo.getPassword());
        if (!flag) {
            return Result.isError("连接linux失败");
        }
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


}
