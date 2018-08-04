package com.java.test;

/**
 * java  求解DP
 *   1. 在一个数组arr中，找出一组不相邻的数字，使得最后的和最大
 *      arr[1, 2, 4, 1, 7, 8, 3]
 */
public class Demo01 {

    public static int rec_opt(int[] arr,int i){

        if(i==0){
            return arr[0];
        }else if(i==1){
            return Math.max(arr[0],arr[1]);
        }else{
            int m=rec_opt(arr, i-2)+arr[i];
            int n=rec_opt(arr, i-1);
            return Math.max(m,n);
        }
    }

    //Math.max(m,n);将这个数存到定义的数组中 遍历所有的i

    /**
     * public static void arrayCopy(Object src,int srcPos,Object dest,int destPos,int length)
       将一个数组指定个数元素复制到另外的一个数组中去
        arrayCopy(arr1, 2, arr2, 5, 10)将arr1 的索引为2开始 复制到arr2里索引为5的位置，复制10个元素

       还有一个copyOf
     public static int[] copyOf(int[] original, int newLength) {
     */
    public static int dp_opt(int[] arr){
        int[] newArr=new int[arr.length];
        newArr[0]=arr[0];
        newArr[1]=Math.max(arr[0],arr[1]);

        for(int i=2; i<arr.length;i++){
            int A=newArr[i-2]+arr[i];
            int B=newArr[i-1];
            newArr[i]=Math.max(A,B);
        }
        return newArr[arr.length-1];
    }



    public static void main(String[] args) {
        int arr[]={1,2,4,1,7,8,3};
        System.out.println(rec_opt(arr,arr.length-1));

        System.out.println(dp_opt(arr));
    }
}
