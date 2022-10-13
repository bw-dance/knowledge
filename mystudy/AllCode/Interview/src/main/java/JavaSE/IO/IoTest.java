package JavaSE.IO;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class IoTest {


    public static void main(String[] args) throws IOException {

        try {
//            long begin = System.currentTimeMillis();
//            FileInputStream fis = new FileInputStream("C:\\Users\\DELL\\Desktop\\50万.xlsx");
//            // 创建一个长度为1024的“竹筒”
//            byte[] bbuf = new byte[128];
//            // 用于保存实际读取的字节数
//            int hasRead = 0;
//            // 使用循环来重复“取水”过程
//            while ((hasRead = fis.read(bbuf)) > 0) {
//                // 取出“竹筒”中水滴（字节），将字节数组转换成字符串输入！
//               System.out.println(new String(bbuf, 0, hasRead));
//            }
//            // 关闭文件输入流，放在finally块里更安全
//            fis.close();
//            long end = System.currentTimeMillis();
//            System.out.println((end - begin) / 1000);
//
//            long begin1 = System.currentTimeMillis();
//            FileInputStream fis2 = new FileInputStream("C:\\Users\\DELL\\Desktop\\50万.xlsx");
//
//            BufferedInputStream bufferedReader = new BufferedInputStream(fis2);
//            byte[] bbuf2 = new byte[128];
//            // 用于保存实际读取的字节数
//            int hasRead2 = 0;
//
//            while ((hasRead2 = bufferedReader.read(bbuf2)) > 0) {
//                System.out.println(new String(bbuf, 0, hasRead2));
//            }
//            bufferedReader.close();
//            long end2 = System.currentTimeMillis();
//
//            System.out.println((end2 - begin1) / 1000);

            BufferedReader bufferedReader = new BufferedReader(new FileReader("C:\\Users\\DELL\\Desktop\\50万.xlsx"));
            String  line = null;
            while ((line=bufferedReader.readLine())!=null){
                System.out.println(line);
                System.out.println("-----------------进行了输出-----------------");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
