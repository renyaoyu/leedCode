package com.ryy.nio.nio;

import sun.plugin2.os.windows.SECURITY_ATTRIBUTES;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
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
    public static void main(String[] args) {
        int port = 8080;
        String host = "127.0.0.1";
        new Thread(new ClientRunnable(host,port)).start();
    }
}

class ClientRunnable implements Runnable{

    private String host;
    private int port;
    private Selector selector;
    private SocketChannel socketChannel;
    private volatile boolean stop;

    public ClientRunnable(String host,int port){
        this.host = host;
        this.port = port;
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            doConnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scanner scan = new Scanner(System.in);
        while (!stop){
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    handel(key);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void handel(SelectionKey selectionKey) throws IOException {
        if (selectionKey.isValid()){
            SocketChannel sc = (SocketChannel)selectionKey.channel();
            if (selectionKey.isConnectable()){
                if (sc.finishConnect()){
                    sc.register(selector,SelectionKey.OP_READ);
                    doWrite(sc);
                }
            }
            if (selectionKey.isReadable()){
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                int read = sc.read(byteBuffer);
                if (read > 0){
                    byteBuffer.flip();
                    byte[] bytes = new byte[byteBuffer.remaining()];
                    byteBuffer.get(bytes);
                    String s = new String(bytes, "UTF-8");
                    System.out.println(s);
                }
            }
        }

    }

    public void doConnect() throws IOException {
        if(socketChannel.connect(new InetSocketAddress(host,port))){
            socketChannel.register(selector, SelectionKey.OP_READ);
            doWrite(socketChannel);
        }else {
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }
    }

    public void doWrite(SocketChannel socketChannel) throws IOException {
        byte[] bytes = "nio客户端请求".getBytes();
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
        byteBuffer.put(bytes);
        byteBuffer.flip();
        socketChannel.write(byteBuffer);
    }
}