package org.treegraph.baas.core.util;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Slf4j
public class ShellExecutor {

    private static final int TIME_OUT_SHELL_REMOTE = 30;

    private static final int TIME_OUT_SHELL_LOCAL = 15;

    public static final String USER = "root";

    public static final String HOST_KEY_PATH = "host_key/id_rsa.pem";

    public static void execRemote(String ip, String cmd) {
        execRemote(ip, cmd, null);
    }

    public static void execRemote(String ip, String cmd, String doneTag) {
        Connection conn = null;
        boolean sshBuildFlag = false;
        try {
            conn = new Connection(ip);
            conn.connect();
            String publicKey = FileUtils.readFile(new ClassPathResource(HOST_KEY_PATH));
            sshBuildFlag = conn.authenticateWithPublicKey(USER, publicKey.toCharArray(), null);
        } catch (Exception e){
            throw new RuntimeException("ssh.conn.build.err", e);
        } finally {
            if(!sshBuildFlag && conn != null){
                conn.close();
            }
        }

        Session session = null;
        BufferedReader reader = null;
        boolean execCmdFlag = false;
        try {
            session = conn.openSession();
            session.execCommand(cmd);
            reader = new BufferedReader(new InputStreamReader(session.getStdout(), StandardCharsets.UTF_8));
            String line;
            long startTime = System.currentTimeMillis();
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                log.info("cmd:{} --->resp:{}", cmd, line);
                if ((System.currentTimeMillis() - startTime) / 1000 > TIME_OUT_SHELL_REMOTE) {
                    throw new RuntimeException("ssh.cmd.exec.timeout");
                }
                if(doneTag != null && line.contains(doneTag)) {
                    execCmdFlag = true;
                    break;
                }
            }
            if(doneTag == null){
                execCmdFlag = true;
            }
        } catch (Exception e) {
            throw new RuntimeException("ssh.cmd.exec.err", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e){
                    log.error("ssh.cmd.exec.err", e);
                }
            }
            if(session != null) {
                session.close();
            }
            conn.close();
        }

        if(!execCmdFlag){
            throw new RuntimeException("ssh.cmd.exec.fail");
        }
    }

    public static void execLocal(String cmd) {
        execLocal(cmd, null);
    }

    public static void execLocal(String cmd, String doneTag) {
        BufferedReader reader = null;
        boolean execCmdFlag = false;
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            long startTime = System.currentTimeMillis();
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                log.info("cmd:{} --->resp:{}", cmd, line);
                if ((System.currentTimeMillis() - startTime) / 1000 > TIME_OUT_SHELL_LOCAL) {
                    throw new RuntimeException("local.cmd.exec.timeout");
                }
                if (doneTag != null && line.contains(doneTag)) {
                    execCmdFlag = true;
                    break;
                }
            }
            if(doneTag == null){
                execCmdFlag = true;
            }
        } catch (Exception e) {
            throw new RuntimeException("local.cmd.exec.err", e);
        } finally {
            if(reader != null){
                try {
                    reader.close();
                } catch (Exception e){
                    log.error("local.cmd.exec.err", e);
                }
            }
        }

        if(!execCmdFlag){
            throw new RuntimeException("local.cmd.exec.fail");
        }
    }

    public static void main(String[] args) {
        String remoteCommand = "cd /opt/service/blockchain/120.27.241.71@48778;bash ./auto_deploy.sh";
        execRemote("120.27.241.71", remoteCommand, null);
//        String localCommand = "scp /Users/wangzhenting/Downloads/deployScripts-101_200_183_108.zip root@120.27.241.71:/opt/service/blockchain";
//        localCommand = "ssh root@120.27.241.71 \"cd /opt/service/blockchain/120.27.241.71@48778;bash ./auto_deploy.sh\"";
//        execLocal(localCommand, null);
    }
}