package com.ryy.nio.blockIO;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by daojia on 2018/8/17.
 */
public class BlockIOClient {
    public static void main(String[] args) throws InterruptedException, IOException {
        Socket socket = new Socket("127.0.0.1",6666);
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write("请求服务".getBytes());
        outputStream.flush();
        byte[] bytes = new byte[1024];
        inputStream.read(bytes);
        System.out.println(new String(bytes,"UTF-8"));
    }
}
