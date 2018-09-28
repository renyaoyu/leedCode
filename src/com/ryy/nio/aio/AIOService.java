package com.ryy.nio.aio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * Created by daojia on 2018/9/17.
 */
public class AIOService {

    public AsynchronousServerSocketChannel asynchronousServerSocketChannel;

    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 8080;
        AIOService aioService = new AIOService();
        System.out.println(Thread.currentThread().getName());
        aioService.asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open();
        aioService.asynchronousServerSocketChannel.bind(new InetSocketAddress(port));
        aioService.asynchronousServerSocketChannel.accept(aioService,new AcceptCompletionHandler());
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }
}
class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel,AIOService>{

    @Override
    public void completed(AsynchronousSocketChannel result, AIOService attachment) {
        attachment.asynchronousServerSocketChannel.accept(attachment,this);
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        System.out.println("method:AcceptCompletionHandler.completed:"+Thread.currentThread().getName());
        result.read(byteBuffer,byteBuffer,new ReadCompletionHandler(result));
    }

    @Override
    public void failed(Throwable exc, AIOService attachment) {
    }
}

class ReadCompletionHandler implements CompletionHandler<Integer,ByteBuffer>{

    private AsynchronousSocketChannel channel;

    public ReadCompletionHandler(AsynchronousSocketChannel channel){
        this.channel = channel;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        System.out.println("method:ReadCompletionHandler.completed:"+Thread.currentThread().getName());
        attachment.flip();
        byte[] bytes = new byte[attachment.remaining()];
        attachment.get(bytes);
        try {
            String s = new String(bytes, "UTF-8");
            System.out.println(s);
            doWrite("收到");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        try {
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doWrite(String resopnse){
        byte[] bytes = resopnse.getBytes();
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
        byteBuffer.put(bytes);
        byteBuffer.flip();
        System.out.println("method:ReadCompletionHandler.doWrite:"+Thread.currentThread().getName());
        channel.write(byteBuffer, byteBuffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                System.out.println("method:ReadCompletionHandler.doWrite.write.completed:"+Thread.currentThread().getName());
                if (attachment.hasRemaining()){
                    channel.write(attachment,attachment,this);
                }else {
                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                    channel.read(readBuffer, readBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                        @Override
                        public void completed(Integer result, ByteBuffer attachment) {
                            System.out.println("method:ReadCompletionHandler.doWrite.write.completed.read.completed:"+Thread.currentThread().getName());
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
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
