package cn.lutfly.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
public class Test6 {

    public static void main(String[] args) throws IOException {

        Thread.currentThread().setName("boss");
        Selector boss = Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.bind(new InetSocketAddress(9999));
        ssc.register(boss, SelectionKey.OP_ACCEPT, null);
        log.info("boss start...");

        Worker worker = new Worker("worker-0");

        while (true) {
            boss.select();
            Iterator<SelectionKey> iter = boss.selectedKeys().iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();

                if (key.isAcceptable()) {
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel sc = channel.accept();
                    log.info("connect... {}", sc);
                    sc.configureBlocking(false);
                    log.info("register ...");
                    worker.register(sc);
                }

            }

        }


    }

    static class Worker implements Runnable {
        private Thread thread;
        private Selector selector;
        private String name;
        private ConcurrentLinkedQueue<Runnable> queue = new ConcurrentLinkedQueue<>();


        public Worker(String name) {
            this.name = name;
            try {
                selector = Selector.open();
            } catch (IOException e) {
                e.printStackTrace();
            }
            thread = new Thread(this);
            thread.start();
        }

        public void register(SocketChannel sc) {
            queue.add(() -> {
                try {
                    sc.register(selector, SelectionKey.OP_READ);
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                }
            });
            selector.wakeup();
        }

        @Override
        public void run() {
            try {
                while (true) {
                    selector.select();
                    while (!queue.isEmpty()) {
                        queue.poll().run();
                    }
                    Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                    while (iter.hasNext()) {
                        SelectionKey skey = iter.next();
                        iter.remove();
                        if (skey.isReadable()) {
                            SocketChannel channel = (SocketChannel) skey.channel();
                            ByteBuffer buffer = ByteBuffer.allocate(16);
                            int read = channel.read(buffer);
                            if (read == -1) {
                                skey.cancel();
                                continue;
                            }
                            buffer.flip();
                            System.out.println(Charset.defaultCharset().decode(buffer));
                            buffer.clear();

                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


