package cn.lutfly;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class Test3 {

    public static void main(String[] args) throws IOException, InterruptedException {

        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress("127.0.0.1",9899));
//        ByteBuffer buf = Charset.defaultCharset().encode(stringBuilder.toString());
//        System.out.println("发送");
//        sc.write(buf);
        System.out.println();
//     sc.write(Charset.defaultCharset().encode("Hello!"););

    }

}
