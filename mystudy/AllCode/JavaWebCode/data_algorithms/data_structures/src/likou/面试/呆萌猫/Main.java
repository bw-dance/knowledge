package likou.面试.呆萌猫;


public class Main {


    public static void main(String[] args) {
        PlayerManager playerManager = new PlayerManagerImpl();
        playerManager.broadcast("同学们好，准备上课了,快来教室！！！！");
        //并发添加用户
        for (int i = 0; i < 10; i++) {
            final int j = i;
            new Thread(() -> {
                Player zhangSan = new PlayerImpl("张三" + j);
                Player liSi = new PlayerImpl("李四" + j);
                liSi.setOffline(true);
                playerManager.addPlayer(zhangSan);
            }).start();
        }
        playerManager.getPlayer("李四" + 5);
        System.out.println("60s后，即可查看下线用户");
        new Thread(() -> {
            int num = 60;
            while (num>0) {
                    System.out.println(num--);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
            }
        }).start();


    }
}
