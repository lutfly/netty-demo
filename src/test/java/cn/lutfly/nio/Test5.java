package cn.lutfly.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Test5 {

    public static void main(String[] args) throws IOException {
        ReentrantReadWriteLock rw = new ReentrantReadWriteLock();
        ReentrantReadWriteLock.ReadLock r = rw.readLock();
        ReentrantReadWriteLock.WriteLock w = rw.writeLock();

        new Thread(()->{
            LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(1));
            w.lock();
            System.out.println("aaa");
            w.unlock();
        }).start();

        w.lock();
//        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(2));
        System.out.println("do something");
        r.lock();
        System.out.println("lock downgrading");
        w.unlock();
        System.out.println("read lock downgrading");
        r.unlock();

    }
}
