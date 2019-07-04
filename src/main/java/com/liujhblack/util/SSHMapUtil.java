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

    private SSHMapUtil() {
    }

    public synchronized static void addSSHConnection(String hostname, Connection connection) {
        sshConnectionMap.put(hostname, connection);
    }

    public synchronized static Connection getSSHConnection(String hostname) {
        return sshConnectionMap.get(hostname);
    }

    public synchronized static void removeSSHConnection(String hostname) {
        sshConnectionMap.remove(hostname);
    }

    public synchronized static void addSSHSession(String hostname, Session session) {
        sshSessionMap.put(hostname, session);
    }

    public synchronized static Session getSSHSession(String hostname) {
        return sshSessionMap.get(hostname);
    }

    public synchronized static void removeSSHSession(String hostname) {
        sshSessionMap.remove(hostname);
    }


}
