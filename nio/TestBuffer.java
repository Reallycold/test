package nio;


import org.testng.annotations.Test;

import java.nio.ByteBuffer;


/**
 * 直接缓冲区 与非直接缓冲区
 * 直接缓冲区可以调用此类的allocateDirect()工厂方法来创建，此方法返回的缓冲区进行分配和取消分配所需要的成本
 * 还可以通过FileChannel的map()方法，将文件区域直接映射到内存
 *
 * Channel表示IO源与目标打开的连接
 * Channel类似于传统的流 只不过Channel本身不能直接访问数据 Channel只能与Buffer进行交互
 * FileChannel //用于读取 写入 映射和操作文件的通道
 * DatagramChannel 通过UDP读取网络中的数据通道
 * SocketChannel   通过TCP读写网络中的数据
 * ServerSocketChannel  可以监听新进的TCP连接 对每一个新进来的连接都会创建一个SocketChannel
 * 获取通道的方式 是支持通道对象调用getChannel（）方法，支持通道的类
 * FileInputStream
 * FileOutputStream
 * RandomAccessFile
 * Socket
 * DatagramSocket
 * ServerSocket
 * 获取通道的其他方式 是使用Files类的静态方法newByteChannel()或者通过静态的open()打开并返回指定通道
 *
 */


public class TestBuffer {
    @Test
    public void test01(){
        String str="abcde";
        //1.分配一个指定大小的缓冲区
        ByteBuffer buf=ByteBuffer.allocate(1024);
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());

        //2.利用put()存入到数据缓冲区
        buf.put(str.getBytes());
        System.out.println("=======put=========");
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());

        //3.切换读取数据模式
        buf.flip();
        System.out.println("===flip()===");
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());

        //4.利用get()读取缓冲区中的数据
        byte[] dst=new byte[buf.limit()];
        buf.get(dst);
        System.out.println(new String(dst,0,dst.length));
        System.out.println("===get()===");
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());

        //5.rewind():可重复读
        buf.rewind();
        System.out.println("===rewind()===");
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());

        //6.clear()
        System.out.println("===clear()===");
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());

        System.out.println((char) buf.get());
    }
}
