package servlet_ready_lession.servlet2021_12_21.filehandle;


import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class DownLoadUtils {
    public static String getFileName(String agent, String filename) throws UnsupportedEncodingException {
        if (agent.contains("MSIE")) {
// IEä¯ÀÀÆ÷
            filename = URLEncoder.encode(filename, "utf-8");
            filename = filename.replace("+", "");
        } else if (agent.contains("Firefox")) {
//»ðºüä¯ÀÀÆ÷
            BASE64Encoder base64Encoder = new BASE64Encoder();
            filename = "=?utf-8?B?" + base64Encoder.encode(filename.getBytes("utf-8")) + "?=";
        } else {
// ÆäËüä¯ÀÀÆ÷
            filename = URLEncoder.encode(filename, "utf-8");

        }
        return filename;
    }
}