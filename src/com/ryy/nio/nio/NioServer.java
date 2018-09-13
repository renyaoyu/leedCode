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
    public static void main(String[] args) {
        int port = 8080;
        new Thread(new ServerRunnable(port)).start();
    }
}

class ServerRunnable implements Runnable {

    private Selector selector;

    private ServerSocketChannel channel;

    private volatile boolean stop;

    public ServerRunnable(int port) {
        try {
            selector = Selector.open();
            channel = ServerSocketChannel.open();
            channel.configureBlocking(false);
            channel.socket().bind(new InetSocketAddress(port),1024);
            channel.register(selector,SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop(){
        this.stop = true;
    }

    @Override
    public void run() {
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

    private void handel(SelectionKey selectionKey) throws IOException {
        if (selectionKey.isValid()){
            if (selectionKey.isAcceptable()){
                ServerSocketChannel ssc = (ServerSocketChannel)selectionKey.channel();
                SocketChannel sc = ssc.accept();
                sc.configureBlocking(false);
                sc.register(selector,SelectionKey.OP_READ);
            }else if (selectionKey.isReadable()){
                SocketChannel sc = (SocketChannel)selectionKey.channel();
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                int read = sc.read(byteBuffer);
                if (read > 0){
                    byteBuffer.flip();
                    byte[] bytes = new byte[byteBuffer.remaining()];
                    byteBuffer.get(bytes);
                    String query = new String(bytes, "UTF-8");
                    System.out.println("server receive :"+query);
                    doWrite(sc,"get the query :"+query);
                }else if (read < 0){
                    selectionKey.cancel();
                    sc.close();
                }
            }
        }
    }

    private void doWrite(SocketChannel socketChannel,String response) throws IOException {
        byte[] bytes = response.getBytes();
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
        byteBuffer.put(bytes);
        byteBuffer.flip();
        socketChannel.write(byteBuffer);
    }
}
