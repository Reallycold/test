package com.java.test;

public class Demo03 {
    public static int findGreatestSumOfSubArray(int[] array){
        /**
         * 累加 如果对于A 若是A的左边累计数非负 加上A后的值不小于A 认为当前累加值有贡献
          */
        if(array.length==0)
            return 0;
        else {
            int total=array[0];
            int maxSum=array[0];
            for (int i = 1; i < array.length; i++) {
                if(total>0)
                    total+=array[i];
                else
                    total=array[i];
                if(total>maxSum)
                    maxSum=total;
            }
            return maxSum;
        }
    }

    public static void main(String[] args) {
        int arr[]={6,-3,-2,7,-15,1,2,2};

        System.out.println(findGreatestSumOfSubArray(arr));
    }
}
