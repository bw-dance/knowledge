[蓝桥杯大赛历届真题 - Java 大学 B 组 - 蓝桥云课 (lanqiao.cn)](https://www.lanqiao.cn/courses/2786/learning/?id=280830)

# A. ASC

![image-20220321153708476](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220321153708476.png)

L 79

# B.卡片

![image-20220406112623298](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220406112623298.png)

结果：3181

方式一：使用excel

方式二：

```java
	public static int count() {
		int res = 0;
		for (int i = 1; i <= 3181; i++) {
			char[] num = String.valueOf(i).toCharArray();
			for (int j = 0; j < num.length; j++) {
				if (num[j] == '1') {
					res++;
				}
			}
		}
		return res;
	}	
```

# C:直线

[(112条消息) 「蓝桥杯」直线（Java）_小成同学_的博客-CSDN博客](https://blog.csdn.net/weixin_53407527/article/details/123833189?spm=1001.2101.3001.6650.3&utm_medium=distribute.pc_relevant.none-task-blog-2~default~CTRLIST~Rate-3.pc_relevant_default&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2~default~CTRLIST~Rate-3.pc_relevant_default&utm_relevant_index=6)

![image-20220406142505274](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220406142505274.png)

```java
public class Main {

    static final int N = 200010;
    static Line[] line = new Line[N];
    static int n;

    public static void main(String[] args) {
        for (int x1 = 0; x1 <= 19; x1++) {
            for (int y1 = 0; y1 <= 20; y1++) {
                for (int x2 = 0; x2 <= 19; x2++) {
                    for (int y2 = 0; y2 <= 20; y2++) {
                        if (x1 != x2) {
                            double k = (double) (y2 - y1) / (x2 - x1);
                            double b = k * x1 - y1;
                            line[n++] = new Line(k, b);
                        }
                    }
                }
            }
        }

        Arrays.sort(line, 0, n);

        int res = 1;
        for (int i = 1; i < n; i++) {
            // 求斜率或者截距不等
            if (Math.abs(line[i].k - line[i - 1].k) > 1e-8 || Math.abs(line[i].b - line[i - 1].b) > 1e-8) {
                res++;
            }
        }

        System.out.println(res + 20); // 最后要加上20
    }

    static class Line implements Comparable<Line> {
        double k; // 斜率
        double b; // 截距

        public Line(double k, double b) {
            this.k = k;
            this.b = b;
        }

        @Override
        public int compareTo(Line o) {
            if (this.k > o.k) return 1;
            if (this.k == o.k) {
                if (this.b > o.b) return 1;
                return -1;
            }
            return -1;
        }
    }
}
```

# D:货物摆放



# F:时间显示

[时间显示 - 蓝桥云课 (lanqiao.cn)](https://www.lanqiao.cn/problems/1452/learning/)

```java
public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		long time = sc.nextLong();
		long ss = 1000L;
		long mm = ss * 60;
		long hh = mm * 60;
		long dd = hh * 24;
		long last = time % dd;
		long hour = last / hh;
		long minute = last % hh / mm;
		long seconds = last % hh % mm / ss;
		System.out.printf("%02d:%02d:%02d", hour, minute, seconds);
	}

	// 方法二：api测试  不通过
	private static String getDate(Long time) {
		Date date = new Date(time);
		SimpleDateFormat format = new SimpleDateFormat("HH:mm::ss", new Locale("CHINA"));
		format.setTimeZone(TimeZone.getTimeZone("GMT"));
		return format.format(date);
	}

}
```

# G:最少砝码

[(131条消息) 第十二届蓝桥杯省赛JavaB组 试题 G: 最少砝码_小小风0的博客-CSDN博客_最少砝码 蓝桥杯](https://blog.csdn.net/Striver00/article/details/116031667)

```java
public class G最小砝码 {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		sc.close();
		int weight = 1;
		int count = 1;
		int total = 1;
		while (total < n) {
			count++;
			weight *= 3;
			total += weight;
		}
		System.out.pr
            intln(count);
		
	}

}

```

