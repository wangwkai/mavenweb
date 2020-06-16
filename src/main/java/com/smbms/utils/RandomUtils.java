package com.smbms.utils;

/**
 * Created by Hunter on 2020-06-01.
 */
public class RandomUtils {

    //1000, 9999
    public static int nextInt(int minNum, int maxNum){
        //[0,9000)
        int num = minNum + (int)(Math.random()*(maxNum-minNum+1));
        return num;
    }
}
