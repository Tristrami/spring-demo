package com.seamew.spel;

public class StringUtil
{
    public static String reverse(String input)
    {
        int len = input.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
        {
            sb.append(input.charAt(len - i - 1));
        }
        return sb.toString();
    }
}
