package nio;

import org.testng.annotations.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class TestBlockingNIO {
    /**
     * Channel:负责连接
     *   SelectableChannel
     *      SocketChannel
     *      DatagramChannel
     *
     * 选择器（Selector）是SelectableChannel的多路复用器 用于监控SelectableChannel的IO状况
     */
    //1.获取通道

    @Test
    public void client() throws IOException {
        SocketChannel sChannel=SocketChannel.open(new InetSocketAddress("127.0.0.1",9898));
        FileChannel inChannel=FileChannel.open(Paths.get("1.txt"), StandardOpenOption.READ);

        //2.分配指定大小的 缓冲区
        ByteBuffer buf=ByteBuffer.allocate(1024);
        //3.读取本地文件，并发送到客户端
        while(inChannel.read(buf)!=-1){
            buf.flip();
            sChannel.write(buf);
            buf.clear();
        }
        inChannel.close();
        sChannel.close();
    }

    @Test
    public void server()throws IOException{
        //1.获取通道
        ServerSocketChannel ssChannel=ServerSocketChannel.open();
        FileChannel outChannel=FileChannel.open(Paths.get("3.txt"),StandardOpenOption.WRITE,StandardOpenOption.CREATE);
        //2.绑定连接
        ssChannel.bind(new InetSocketAddress(9898));
        //3. 获取客户端连接的通道
        SocketChannel sChannel=ssChannel.accept();
        //4.分配指定大小的缓冲区
        ByteBuffer buf=ByteBuffer.allocate(1024);
        //5.接收客户端的数据，并保存到本地
        while(sChannel.read(buf)!=-1){
            buf.flip();
            outChannel.write(buf);
            buf.clear();
        }
        //6.关闭通道
        sChannel.close();
        outChannel.close();
        ssChannel.close();
    }
}
