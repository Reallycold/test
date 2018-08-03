package nio;

import org.testng.annotations.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

public class TestNonBlocking {
    //客户端
    @Test
    public void client() throws IOException{
        //1.获取通道
        SocketChannel sChannel=SocketChannel.open(new InetSocketAddress("127.0.0.1",9898));
        //2.切换非阻塞模式
        sChannel.configureBlocking(false);

        //3.分配缓冲区大小
        ByteBuffer buf=ByteBuffer.allocate(1024);
        //4. 发送数据给服务端
        Scanner sc=new Scanner(System.in);
        while (sc.hasNext()){
            String str=sc.next();
            buf.put((new Date().toString()+"\n"+str).getBytes());
            buf.flip();
            sChannel.write(buf);
            buf.clear();
        }
        //5. 关闭通道
        sChannel.close();
    }

    @Test
    public void server() throws IOException {
        ServerSocketChannel ssChannel=ServerSocketChannel.open();
        ssChannel.configureBlocking(false);
        //3.绑定连接
        ssChannel.bind(new InetSocketAddress(9898));

        //4.获取选择器
        Selector selector=Selector.open();
        //5.将通道注册到选择器上，并且指定"监听接收事件"
        ssChannel.register(selector, SelectionKey.OP_ACCEPT);
        //6.轮询式的获取选择器上以及"准备就绪"的事件
        while (selector.select()>0){
            //7.获取当前选择器所有注册的选择键
            Iterator<SelectionKey> it=selector.selectedKeys().iterator();
            while (it.hasNext()){
                SelectionKey sk=it.next();
                if(sk.isAcceptable()){
                    //10.若接收就绪 获取客户端连接
                    SocketChannel sChannel=ssChannel.accept();
                    sChannel.configureBlocking(false);
                    sChannel.register(selector,SelectionKey.OP_READ);

                }else if(sk.isReadable()){
                    SocketChannel sChannel= (SocketChannel) sk.channel();
                    //14.读取数据
                    ByteBuffer buf=ByteBuffer.allocate(1024);
                    int len=0;
                    while ((len=sChannel.read(buf))!=-1){
                        buf.flip();
                        System.out.println(new String(buf.array(), 0, len));
                        buf.clear();
                    }
                }
                it.remove();
            }
        }
    }

}
