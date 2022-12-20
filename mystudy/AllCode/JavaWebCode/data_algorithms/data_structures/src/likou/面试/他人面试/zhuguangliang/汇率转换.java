package likou.面试.他人面试.zhuguangliang;

import java.util.*;

public class 汇率转换 {
    public static void main(String[] args) {
        //单位：分
        Map<String, Double> huiLv = new HashMap<>();
        huiLv.put("CNY", 100.0);
        huiLv.put("JPY", huiLv.get("CNY") / 1825L);
        huiLv.put("HKD", huiLv.get("CNY") / 123L);
        huiLv.put("EUR", huiLv.get("CNY") / 14L);
        huiLv.put("GBP", huiLv.get("CNY") / 12L);
        huiLv.put("fen", huiLv.get("CNY") / 100);
        huiLv.put("cents", huiLv.get("HKD") / 100);
        huiLv.put("sen", huiLv.get("JPY") / 100);
        huiLv.put("eurocents", huiLv.get("EUR") / 100);
        huiLv.put("pence", huiLv.get("GBP") / 100);

        Scanner scanner = new Scanner(System.in);
        int ciShu = scanner.nextInt();
        int count=0;
        for (int i = 0; i < ciShu; i++) {
            String s = scanner.next();
            byte[] bytes = s.getBytes();
            int jieShu=0;
            for (int j = 0; j < bytes.length; j++) {
                if (s.charAt(j) < 48 || s.charAt(j) > 57) {
                    jieShu=j;
                }
            }
            int sum = 0;

            count+=sum;


        }

    }


}
