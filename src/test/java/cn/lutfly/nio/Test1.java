package cn.lutfly.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

@Slf4j
public class Test1 {
    static Object obj =new Object();
    public static void main(String[] args) {
        File aaa = new File("aaa");

        System.out.println(aaa.getAbsoluteFile());
        try (FileChannel channel = new FileInputStream("aaa").getChannel()) {

            ByteBuffer buffer = ByteBuffer.allocate(10);
            int read = channel.read(buffer);
            System.out.println(read);
            buffer.flip();

            while (buffer.hasRemaining()){
                System.out.println((char)buffer.get());
            }

        } catch (IOException e) {
        }

    }
}
