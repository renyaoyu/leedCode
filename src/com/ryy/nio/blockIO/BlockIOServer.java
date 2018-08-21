package com.ryy.nio.blockIO;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by daojia on 2018/8/17.
 */
public class BlockIOServer {
    public static void main(String[] args) throws InterruptedException, IOException {
        ServerSocket serverSocket = new ServerSocket(6666);
        while (true) {
            Socket accept = serverSocket.accept();
            InputStream inputStream = accept.getInputStream();
            OutputStream outputStream = accept.getOutputStream();
            byte[] bytes = new byte[1024];
            inputStream.read(bytes);
            System.out.println(new String(bytes,"utf-8"));
            outputStream.write("收到".getBytes());
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            accept.close();
        }
    }
}
