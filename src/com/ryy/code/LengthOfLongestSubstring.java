package com.ryy.code;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by daojia on 2018/7/10.
 */
public class LengthOfLongestSubstring {
//    给定一个字符串，找出不含有重复字符的最长子串的长度。
//    示例：
//    给定 "abcaaaaaabcde" ，没有重复字符的最长子串是 "abc" ，那么长度就是3。
//    给定 "bbbbb" ，最长的子串就是 "b" ，长度是1。
//    给定 "pwwkew" ，最长子串是 "wke" ，长度是3。请注意答案必须是一个子串，"pwke" 是 子序列  而不是子串。

    public static void main(String[] args) {

        int x = new LengthOfLongestSubstring().lengthOfLongestSubstring("dvdf");
        System.out.println(x);
    }
    public int lengthOfLongestSubstring(String s) {
        HashSet<Character> characters = new HashSet<>();
        int result = 0;//不重复字符串长度
        int i = 0,j = 0;//两个角标，i为不重复字符串头，j为字符串尾
        char[] chars = s.toCharArray();
        int n = chars.length;
        while (i < n && j < n){//整个循环运行起来类似于"毛毛虫",当j向前试探到重复的字符，j停止，i向前走，当i走到某一个角标时，发现j所在位置在characters中没有重复了，i停止，j继续向前。
            if(!characters.contains(chars[j])){
                characters.add(chars[j++]);
                result = Math.max(result,j-i);
            }else {
                characters.remove(chars[i++]);
            }
        }
        return result;
    }
}
