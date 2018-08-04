package ConcurrentHashMap.test;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 1.为什么要使用ConcurrentHashMap?
 *  在并发编程中 使用HashMap容易造成死循环的
 *  使用线程安全的HashTable效率低下
 *  ConcurrentHashMap是线程安全而且高效的HashMap
 *
 * 2.HashTable容器在激烈的并发环境中表现低下的原因：
 *    HashTable使用synchronize来保证线程安全，所有访问HashTable的线程都必须竞争同一把锁,线程1在使用put
 *    添加元素时，线程2只能等待线程1操作完，不能put元素 也不能get
 *
 *    如果容器里面有多把锁，每一把锁用于锁容器其中一部分数据
 *    当多线程访问容器里面不同数据段的数据时，线程间就不会存在锁竞争，从而可以有效的提高并发访问效率
 *    这就是ConcurrentHashMap的锁分段技术
 *    首先将数据分成一段一段的，为每段数据加锁，当一个线程占用锁访问其他一个段数据的时候，
 *    其他段的数据也能被其他线程访问
 */



public class Demo01 {
    //ConcurrentHashMap
}
