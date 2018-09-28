package com.ryy.nio.aio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * Created by daojia on 2018/9/17.
 */
public class AIOClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 8080;
        String host = "127.0.0.1";
        AIOClient aioClient = new AIOClient();
        new AsyncTimeClientHandlet(host,port);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }
}
class AsyncTimeClientHandlet implements CompletionHandler<Void,AsyncTimeClientHandlet >{

    private AsynchronousSocketChannel socketChannel;
    private String host;
    private int port;

    public AsyncTimeClientHandlet(String host,int port) throws IOException {
        System.out.println(Thread.currentThread().getName());
        this.host = host;
        this.port = port;
        socketChannel = AsynchronousSocketChannel.open();
        socketChannel.connect(new InetSocketAddress(host,port),this,this);
    }

    @Override
    public void completed(Void result, AsyncTimeClientHandlet attachment) {
        System.out.println("method:AsyncTimeClientHandlet.completed:"+Thread.currentThread().getName());
        byte[] bytes = "发送".getBytes();
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
        byteBuffer.put(bytes);
        byteBuffer.flip();
        socketChannel.write(byteBuffer,byteBuffer,new CompletionHandler<Integer,ByteBuffer> (){
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                System.out.println("method:AsyncTimeClientHandlet.completed.write.completed:"+Thread.currentThread().getName());
                if (attachment.hasRemaining()){
                    socketChannel.write(attachment,attachment,this);
                }else {
                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                    socketChannel.read(readBuffer, readBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                        @Override
                        public void completed(Integer result, ByteBuffer attachment) {
                            System.out.println("method:AsyncTimeClientHandlet.completed.write.completed.read.completed:"+Thread.currentThread().getName());
                            attachment.flip();
                            byte[] bytes1 = new byte[attachment.remaining()];
                            attachment.get(bytes1);
                            try {
                                String read = new String(bytes1,"UTF-8");
                                System.out.println(read);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void failed(Throwable exc, ByteBuffer attachment) {

                        }
                    });
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {

            }
        });
    }

    @Override
    public void failed(Throwable exc, AsyncTimeClientHandlet attachment) {

    }
}