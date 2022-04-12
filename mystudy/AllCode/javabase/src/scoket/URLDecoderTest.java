package scoket;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @Classname dd
 * @Description TODO
 * @Date 2022/3/4 9:10
 * @Created by zhq
 */
public class URLDecoderTest
{
    public static void main(String[] args)
            throws Exception
    {
        // 将application/x-www-form-urlencoded字符串
        // 转换成普通字符串
        // 其中的字符串直接从图17.3所示窗口复制过来
        String keyWord = URLDecoder.decode(
                "%E7%96%AF%E7%8B%82%E8%AE%B2%E4%B9%89", "utf-8");
        System.out.println(keyWord);  //疯狂讲义
        // 将普通字符串转换成
        // application/x-www-form-urlencoded字符串
        String urlStr = URLEncoder.encode(
                "疯狂讲义" , "utf-8");
        System.out.println(urlStr); //%E7%96%AF%E7%8B%82%E8%AE%B2%E4%B9%89
    }
}
