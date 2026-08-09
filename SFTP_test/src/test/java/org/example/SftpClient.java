package org.example;

import com.jcraft.jsch.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class SftpClient {
    private String host;
    private int port;
    private String login;
    private String password;
    private Session session;
    private ChannelSftp channel;
    public SftpClient(String host, int port, String login, String password) {
        this.host = host;
        this.port = port;
        this.login = login;
        this.password = password;
    }
    public void connect() throws JSchException {
        JSch jsch = new JSch();
        session = jsch.getSession(login, host, port);
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();
        Channel ch = session.openChannel("sftp");
        ch.connect();
        this.channel = (ChannelSftp) ch;
        System.out.println("Успешно! Вы подключились к " + host + ":" + port);
    }

    public String downloadFile(String remotePath) throws Exception {
        try (InputStream in = channel.get(remotePath);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            String content = out.toString("UTF-8");
            System.out.println("Успешно! Вы скачали файл: " + remotePath);
            return content;
        }
    }

    public void uploadFile(String remotePath, String content) throws Exception {
        byte[] bytes = content.getBytes("UTF-8");
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        channel.put(in, remotePath);
        System.out.println("Успешно! Файл загружен в папку: " + remotePath);
    }

    public void disconnect() {
        if (channel != null && channel.isConnected()) channel.disconnect();
        if (session != null && session.isConnected()) session.disconnect();
        System.out.println("Сессия завершена");
    }
}