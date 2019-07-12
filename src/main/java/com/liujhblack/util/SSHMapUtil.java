package com.liujhblack.util;/**
 * Created by liujunhui on 2019/7/2.
 */

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import com.liujhblack.domain.SSHInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author liujunhui
 * @Date 2019/7/2 16:25
 * @Description TODO
 */
public class SSHMapUtil {

    private static Map<String, Connection> sshConnectionMap = new HashMap<>();
    private static Map<String, Session> sshSessionMap = new HashMap<>();
    private static Map<String, SSHInfo> sshInfoMap = new HashMap<>();

    private SSHMapUtil() {
    }

    public synchronized static void addSSHConnection(String randomString, Connection connection) {
        sshConnectionMap.put(randomString, connection);
    }

    public synchronized static Connection getSSHConnection(String randomString) {
        return sshConnectionMap.get(randomString);
    }

    public synchronized static void removeSSHConnection(String randomString) {
        sshConnectionMap.remove(randomString);
    }

    public synchronized static void addSSHSession(String randomString, Session session) {
        sshSessionMap.put(randomString, session);
    }

    public synchronized static Session getSSHSession(String randomString) {
        return sshSessionMap.get(randomString);
    }

    public synchronized static void removeSSHSession(String randomString) {
        sshSessionMap.remove(randomString);
    }

    public synchronized static void addSSHInfo(String randomString, SSHInfo sshInfo) {
        sshInfoMap.put(randomString, sshInfo);
    }

    public synchronized static SSHInfo getSSHInfo(String randomString) {
        return sshInfoMap.get(randomString);
    }

    public synchronized static void removeSSHInfo(String randomString) {
        sshInfoMap.remove(randomString);
    }


}
