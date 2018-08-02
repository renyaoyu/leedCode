package com.ryy.code;

/**
 * Created by daojia on 2018/8/1.
 */
public class InversionInteger {
    public static void main(String[] args) {
        int reverse = reverse(-2147483648);
        System.out.println(reverse);
    }

    /**
     * 给定一个 32 位有符号整数，将整数中的数字进行反转。
     示例 1:
     输入: 123
     输出: 321

     示例 2:
     输入: -123
     输出: -321

     示例 3:
     输入: 120
     输出: 21
     注意:
     假设我们的环境只能存储 32 位有符号整数，其数值范围是 [Integer.MIN_VALUE,  Integer.MAX_VALUE]。根据这个假设，如果反转后的整数溢出，则返回 0。
     * @param x
     * @return
     */
    public static int reverse(int x) {
        String s = "";
        if (x >= 0){
            s = "+"+x;
        }else {
            s = x+"";
        }
        char[] src = s.toCharArray();
        char[] des =  new char[s.length()];
        for (int i = 0; i< src.length; i++){
            if (i == 0){
                des[0] = src[i];
            }else {
                des[i] = src[src.length - i];
            }
        }
        String s1 = new String(des);
        Long result = new Long(s1);
        Long max = new Long(Integer.MAX_VALUE);
        Long min = new Long(Integer.MIN_VALUE);
        if (result > max || result < min){
            return 0;
        }
        return result.intValue();
    }
}
