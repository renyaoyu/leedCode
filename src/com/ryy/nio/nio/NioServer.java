package com.ryy.nio.nio;

import com.sun.xml.internal.stream.util.ThreadLocalBufferAllocator;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by daojia on 2018/9/12.
 */
public class NioServer {

    public Selector selector;

    public ServerSocketChannel channel;

    int port = 8080;

    public NioServer() throws IOException {
        selector = Selector.open();
        channel = ServerSocketChannel.open();
        channel.configureBlocking(false);
        channel.socket().bind(new InetSocketAddress(port));
        channel.register(selector,SelectionKey.OP_ACCEPT);
    }

    public static void main(String[] args) throws IOException {
        NioServer nioServer = new NioServer();
        while (true){
            nioServer.selector.select(1000);
            Set<SelectionKey> selectionKeys = nioServer.selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                iterator.remove();
                nioServer.handle(key);
            }
        }

    }

    public void handle(SelectionKey key) throws IOException {
        System.out.println("handle");
        if (key.isValid()){
            if (key.isAcceptable()){
                ServerSocketChannel ssc = (ServerSocketChannel)key.channel();
                SocketChannel sc = ssc.accept();
                sc.configureBlocking(false);
                sc.register(selector,SelectionKey.OP_READ);
            }else if (key.isReadable()){
                SocketChannel sc = (SocketChannel) key.channel();
                String s = doRead(key);
                doWrite(key,s);
//                sc.register(selector,SelectionKey.OP_READ);
            }else if (key.isWritable()){

            }
        }
    }

    public String doRead(SelectionKey key) throws IOException {
        System.out.println("doRead");
        SocketChannel sc = (SocketChannel)key.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int read = sc.read(byteBuffer);
        String result = "";
        if (read > 0) {
            byteBuffer.flip();
            byte[] bytes = new byte[byteBuffer.remaining()];
            byteBuffer.get(bytes);
            result = new String(bytes, "UTF-8");
            System.out.println("服务端接收消息:" + result);
        }
        return result;
    }

    public void doWrite(SelectionKey key,String string) throws IOException {
        System.out.println("doWrite");
        SocketChannel sc = (SocketChannel)key.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byte[] receive = (string).getBytes();
        byteBuffer.put(receive);
        byteBuffer.flip();
        sc.write(byteBuffer);
    }
}
