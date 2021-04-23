package cn.lutfly.nio;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class Test3 {

    public static void main(String[] args) throws IOException, InterruptedException {

        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress("127.0.0.1",9999));
        ByteBuffer buf = Charset.defaultCharset().encode("hello!");
        System.out.println("发送");
        sc.write(buf);
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(3));
        sc.close();
        System.out.println();
//     sc.write(Charset.defaultCharset().encode("Hello!"););


        ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 1024);
    }

}
