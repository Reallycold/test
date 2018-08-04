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

public class TestBlockingNIO2 {
    //1.客户端
    @Test
     public void client() throws IOException {
        SocketChannel sChannel=SocketChannel.open(new InetSocketAddress("127.0.0.1",9898));

        FileChannel inChannel=FileChannel.open(Paths.get("1.txt"), StandardOpenOption.READ);
        ByteBuffer buf=ByteBuffer.allocate(1024);
        while(inChannel.read(buf)!=-1){
            buf.flip();
            sChannel.write(buf);
            buf.clear();
        }
        sChannel.shutdownOutput();

        //接收服务端的反馈
        int len=0;
        while ((len=sChannel.read(buf))!=-1){
            buf.flip();
            System.out.println(new String(buf.array(),0,len));
            buf.clear();
        }
        inChannel.close();
        sChannel.close();
    }

    @Test
    public void server() throws IOException {
        ServerSocketChannel ssChannel=ServerSocketChannel.open();
        FileChannel outChannel=FileChannel.open(Paths.get("4.txt"),StandardOpenOption.CREATE,StandardOpenOption.WRITE);

        ssChannel.bind(new InetSocketAddress(9898));
        SocketChannel sChannel=ssChannel.accept();
        ByteBuffer buf=ByteBuffer.allocate(1024);

        while (sChannel.read(buf)!=-1){
            buf.flip();
            outChannel.write(buf);
            buf.clear();

        }
        buf.put("服务端接收数据成功".getBytes());
        buf.flip();
        sChannel.write(buf);

        sChannel.close();
        outChannel.close();
        ssChannel.close();
    }

}