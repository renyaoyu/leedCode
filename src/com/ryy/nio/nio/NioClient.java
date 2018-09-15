package com.ryy.nio.nio;

import sun.plugin2.os.windows.SECURITY_ATTRIBUTES;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by daojia on 2018/9/12.
 */
public class NioClient {
    public int port = 8080;
    public String host = "127.0.0.1";
    public Selector selector;
    public SocketChannel socketChannel;
    public NioClient() throws IOException {
        selector = Selector.open();
        socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
    }
    public static void main(String[] args) throws IOException {
        NioClient nioClient = new NioClient();
        nioClient.doConnect();
        while (true){
            nioClient.selector.select(1000);
            Set<SelectionKey> keys = nioClient.selector.keys();//这个是所有注册在 selector上的channel
            Set<SelectionKey> selectionKeys = nioClient.selector.selectedKeys();//这个是selector上准备就绪的channel
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                iterator.remove();
                nioClient.handel(key);
                key.cancel();
            }
        }
    }

    public void handel(SelectionKey key) throws IOException {
        System.out.println("handel");
        if (key.isValid()){
            SocketChannel sc = (SocketChannel)key.channel();
            if (key.isReadable()){
                String s = doRead(sc);
                doWrite(sc,s);
//                sc.register(selector,SelectionKey.OP_READ);
            }else if (key.isWritable()){

            }else if (key.isConnectable()){
                if (sc.finishConnect()){
                    sc.register(selector,SelectionKey.OP_READ);
                    doWrite(sc,"连接");
                }
            }
        }
    }

    public String doRead(SocketChannel sc) throws IOException {
        System.out.println("doRead");
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int read = sc.read(byteBuffer);
        String result = "";
        if (read > 0) {
            byteBuffer.flip();
            byte[] bytes = new byte[byteBuffer.remaining()];
            byteBuffer.get(bytes);
            result = new String(bytes, "UTF-8");
            System.out.println("客户端接收消息:" + result);
        }
        return result;
    }

    public void doWrite(SocketChannel sc,String string) throws IOException {
        System.out.println("doWrite");
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byte[] receive = (string).getBytes();
        byteBuffer.put(receive);
        byteBuffer.flip();
        sc.write(byteBuffer);
    }

    public void doConnect() throws IOException {
        System.out.println("doConnect");
        if(socketChannel.connect(new InetSocketAddress(host,port))){
            socketChannel.register(selector, SelectionKey.OP_READ);
            doWrite(socketChannel,"连接");
        }else {
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }

    }
}