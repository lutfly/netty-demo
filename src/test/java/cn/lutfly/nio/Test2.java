package cn.lutfly.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

@Slf4j
public class Test2 {

    public static void main(String[] args) throws IOException {

        Selector selector = Selector.open();

        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(9899));
        ssc.configureBlocking(false);
        SelectionKey sscKey = ssc.register(selector, 0, null);
        log.info("sscKey:   "+sscKey.toString());
        sscKey.interestOps(SelectionKey.OP_ACCEPT);

        while (true) {
            log.info("waiting...");
            selector.select();

            Iterator<SelectionKey> ite = selector.selectedKeys().iterator();
            while (ite.hasNext()) {
                SelectionKey key = ite.next();
                ite.remove();
                log.info(key.toString());

                if (key.isAcceptable()) {
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel sc = channel.accept();
                    sc.configureBlocking(false);
                    log.info(sc.toString());
                    SelectionKey scKey = sc.register(selector, 0, null);
                    scKey.interestOps(SelectionKey.OP_READ);
                    log.info(scKey.toString());
                } else if (key.isReadable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(16);
                    log.info("等待读");
                    int read = channel.read(buffer);
                    log.info("##################  "+read);
//                    if (read!=-1){
//                    buffer.flip();
//                    while (buffer.hasRemaining()) {
//                        System.out.print((char) buffer.get());
//                    }
//                    System.out.println();
                    buffer.clear();
                    key.cancel();
//                    }else {
//                        key.cancel();
//                    }
                }
            }

        }
    }

}
