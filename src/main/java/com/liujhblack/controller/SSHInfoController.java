package com.liujhblack.controller;/**
 * Created by liujunhui on 2019/7/2.
 */

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import com.liujhblack.domain.SSHInfo;
import com.liujhblack.util.Result;
import com.liujhblack.util.SSHMapUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.UUID;

/**
 * @Author liujunhui
 * @Date 2019/7/2 16:34
 * @Description TODO
 */
@RestController
public class SSHInfoController {

    @PostMapping("/sshInfo")
    public Result save(SSHInfo sshInfo){
        try {
            Connection connection = new Connection(sshInfo.getHostname());
            connection.connect();
            boolean flag = connection.authenticateWithPassword(sshInfo.getUsername(), sshInfo.getPassword());
            if (!flag) {
                return Result.isError("连接linux失败");
            }
            Session session = connection.openSession();
            session.requestPTY("bash");
            session.startShell();

            String randomString = UUID.randomUUID().toString().replace("-", "");
            SSHMapUtil.addSSHConnection(randomString,connection);
            SSHMapUtil.addSSHSession(randomString,session);

            return Result.isSuccess(randomString);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.isError("连接linux失败");
        }

    }
}
