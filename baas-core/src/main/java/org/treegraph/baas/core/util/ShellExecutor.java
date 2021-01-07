package org.treegraph.baas.core.util;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Slf4j
public class ShellExecutor {

    private static final int TIME_OUT_SHELL = 10;

    public static final String USER = "root";

    public static final String HOST_KEY_PATH = "host_key/id_rsa.pem";

    public static void execRemote(String ip, String cmd) {
        execRemote(ip, cmd, null);
    }

    public static void execRemote(String ip, String shell, String doneTag) {
        Connection conn = null;
        boolean sshBuildFlag = false;
        try {
            conn = new Connection(ip);
            conn.connect(null, 3000, 3000);
            String publicKey = FileUtils.readFile(new ClassPathResource(HOST_KEY_PATH));
            sshBuildFlag = conn.authenticateWithPublicKey(USER, publicKey.toCharArray(), null);
        } catch (Exception e){
            throw new RuntimeException("shell:" + shell + ",ssh.conn.build.err", e);
        } finally {
            if(!sshBuildFlag && conn != null){
                conn.close();
            }
        }

        Session session = null;
        BufferedReader reader = null;
        boolean execResult;
        try {
            session = conn.openSession();
            session.execCommand(shell);
            reader = new BufferedReader(new InputStreamReader(session.getStdout(), StandardCharsets.UTF_8));
            execResult = validateResponse(shell, doneTag, reader);
        } catch (Exception e) {
            throw new RuntimeException("shell:" + shell + ",ssh.shell.exec.err", e);
        } finally {
            try {
                if (reader != null) { reader.close();}
                if(session != null) { session.close();}
                conn.close();
            } catch (Exception e){
            }
        }
        if(!execResult){
            throw new RuntimeException("shell:" + shell + ",ssh.shell.exec.fail");
        }
    }

    public static void execLocal(String cmd) {
        execLocal(cmd, null);
    }

    public static void execLocal(String shell, String doneTag) {
        BufferedReader reader = null;
        boolean execResult;
        try {
            Process process = Runtime.getRuntime().exec(shell);
            reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            execResult = validateResponse(shell, doneTag, reader);
        } catch (Exception e) {
            throw new RuntimeException("shell:" + shell + ",local.shell.exec.err", e);
        } finally {
            try {
                if(reader != null){ reader.close();}
            } catch (Exception e){
            }
        }
        if(!execResult){
            throw new RuntimeException("shell:" + shell + ",local.shell.exec.fail");
        }
    }

    private static boolean validateResponse(String cmd, String doneTag, BufferedReader reader) throws IOException {
        boolean execCmdFlag = false;
        StringBuilder resp = new StringBuilder();
        String line;
        long startTime = System.currentTimeMillis();
        while ((line = reader.readLine()) != null) {
            resp.append(line).append("\n");
            if ((System.currentTimeMillis() - startTime) / 1000 > TIME_OUT_SHELL) {
                break;
            }
            if(doneTag != null && line.contains(doneTag)) {
                execCmdFlag = true;
                break;
            }
        }
        if(doneTag == null){
            execCmdFlag = true;
        }
        log.info("shell:{},resp:{}", cmd, resp.toString());
        return execCmdFlag;
    }
}