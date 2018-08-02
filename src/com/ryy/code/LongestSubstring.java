package com.ryy.code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by daojia on 2018/8/1.
 */
public class LongestSubstring {
    /**
     * 给定一个字符串 s，找到 s 中最长的回文子串。你可以假设 s 的最大长度为1000。
     * 示例 1：
     * 输入: "babad"
     * 输出: "bab"
     * 注意: "aba"也是一个有效答案。
     * <p>
     * 示例 2：
     * 输入: "cbbd"
     * 输出: "bb"
     */
    public static void main(String[] args) {
        String babad = longestPalindrome("abacfghj");
        System.out.println(babad);
    }

    public static String longestPalindrome(String s) {//最长回文串
        char[] source = s.toCharArray();
        char[] desc = new char[source.length];
        for (int i = 0; i < source.length; i++) {
            desc[source.length - i] = source[i];
        }
        int start = 0;
        int end = 0;
        int maxLength = 0;
        for (int i = 0; i < desc.length; i++) {
            for (int j = 0; j < desc.length - i; j++) {
                String temp = String.copyValueOf(desc, i, j);
                int index = s.indexOf(temp);
                if (index >= 0) {

                }
            }

        }
    }


}
