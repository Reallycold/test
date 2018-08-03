package nio;


import org.testng.annotations.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Set;

/**
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
 * <p>
 * 一键格式化代碼： Ctrl+Alt+L
 * try--catch     Ctrl+Alt+t
 *
 */
public class TestChannel {


    //字符集
    @Test
    public void test6() throws CharacterCodingException {
        Charset cs1=Charset.forName("GBK");
        //获取编码器
        CharsetEncoder ce=cs1.newEncoder();

        CharsetDecoder cd=cs1.newDecoder();

        CharBuffer cBuf=CharBuffer.allocate(1024);

        cBuf.put("天天向上");
        cBuf.flip();
        ByteBuffer bBuf=ce.encode(cBuf);

        for(int i=0; i<12;i++){
            System.out.println(bBuf.get());
        }
        //解码
        bBuf.flip();
        CharBuffer cBuf2=cd.decode(bBuf);
        System.out.println(cBuf2.toString());

       // System.out.println("==========");
        //Charset cs2=Charset.forName("GBK")


    }



    @Test
    public void test5(){
        Map<String, Charset> map=Charset.availableCharsets();
        Set<Map.Entry<String, Charset>> set = map.entrySet();
        for(Map.Entry<String, Charset> entry:set){
            System.out.println(entry.getKey()+"= "+entry.getValue());
        }
    }




    //分散和聚集

    /**
     * 分散读取： 是指从Channel中读取的数据 分散到多个Buffer中
     * 聚集写入： 是指将多个Buffer中的数据聚集到Channel
     */
    @Test
    public void test4()throws IOException {
        long start=System.currentTimeMillis();

        RandomAccessFile raf1=new RandomAccessFile("1.txt","rw");
        FileChannel channel1=raf1.getChannel();

        //分配指定大写的缓冲区
        ByteBuffer buf1=ByteBuffer.allocate(100);
        ByteBuffer buf2=ByteBuffer.allocate(1024);

        //3.分散读取
        ByteBuffer[] bufs={buf1, buf2};
        for(ByteBuffer byteBuffer:bufs){
            byteBuffer.flip();
        }

        //System.out.println(new String(bufs[0].array(), 0 , bufs[0].limit()));
        System.out.println("-----------------");
       // System.out.println(new String(bufs[1].array(), 0, bufs[1].limit()));
        //4.聚集写入
        RandomAccessFile raf2=new RandomAccessFile("2.txt","rw");
        FileChannel channel2=raf2.getChannel();
        channel2.write(bufs);
    }

    @Test //通道之间的传输(直接缓冲区）107
    public void test3()throws IOException {
        long start = System.currentTimeMillis();

        FileChannel inChannel=FileChannel.open(Paths.get("E:/1.avi"), StandardOpenOption.READ);
        FileChannel outChannel=FileChannel.open(Paths.get("E:/4.avi"),StandardOpenOption.WRITE,StandardOpenOption.READ,StandardOpenOption.CREATE);

        outChannel.transferFrom(inChannel,0,inChannel.size());

        inChannel.close();
        outChannel.close();

        long end= System.currentTimeMillis();
        System.out.println("耗费时间："+(end-start));
    }


        @Test //215
   public void test2()throws IOException{
       long start=System.currentTimeMillis();

       FileChannel inChannel=FileChannel.open(Paths.get("E:/1.avi"), StandardOpenOption.READ);
       FileChannel outChannel=FileChannel.open(Paths.get("E:/3.avi"),StandardOpenOption.WRITE,StandardOpenOption.READ,StandardOpenOption.CREATE);

       //内存映射文件
       MappedByteBuffer inMap= inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
       MappedByteBuffer outMap= outChannel.map(FileChannel.MapMode.READ_WRITE, 0, inChannel.size());

       //直接对缓冲区进行数据的读写操作
       byte[] dst=new byte[inMap.limit()];
       inMap.get(dst);
       outMap.put(dst);

       inChannel.close();
       outChannel.close();

       long end= System.currentTimeMillis();
       System.out.println("耗费时间："+(end-start));
   }


    //1.利用通道完成文本复制
    @Test //733
    public void test1(){//10874-10953
        long start = System.currentTimeMillis();

        FileInputStream fis = null;
        FileOutputStream fos = null;
        //①获取通道
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            fis = new FileInputStream("E:/1.avi");
            fos = new FileOutputStream("E:/2.avi");

            inChannel = fis.getChannel();
            outChannel = fos.getChannel();

            //②分配指定大小的缓冲区
            ByteBuffer buf = ByteBuffer.allocate(1024);

            //③将通道中的数据存入缓冲区中
            while(inChannel.read(buf) != -1){
                buf.flip(); //切换读取数据的模式
                //④将缓冲区中的数据写入通道中
                outChannel.write(buf);
                buf.clear(); //清空缓冲区
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(outChannel != null){
                try {
                    outChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(inChannel != null){
                try {
                    inChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        long end = System.currentTimeMillis();
        System.out.println("耗费时间为：" + (end - start));

    }
}
