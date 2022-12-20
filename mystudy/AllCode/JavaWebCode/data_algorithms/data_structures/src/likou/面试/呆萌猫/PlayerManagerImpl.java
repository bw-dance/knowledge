package likou.面试.呆萌猫;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;


public class PlayerManagerImpl implements PlayerManager {
    private ConcurrentHashMap<String, Player> onlinePlayer;

    public PlayerManagerImpl() {
        onlinePlayer = new ConcurrentHashMap<>();
        Timer time = new Timer("定时踢人下线");
        time.schedule(new TimerTask() {
            @Override
            public void run() {
                onlinePlayer.values().removeIf(player -> {
                    String username = player.getUsername();
                    System.out.println("用户：" + username + "下线");
                    return player.isOffline();
                });
            }
        }, new Date(), 1000 * 60L);
    }

    public ConcurrentHashMap<String, Player> getOnlinePlayer() {
        return onlinePlayer;
    }

    @Override
    public void addPlayer(Player player) {
        if (player == null || player.getUsername() == null) {
            System.out.println("不可添加空用户");
            return;
        }
        if (onlinePlayer.contains(player.getUsername())) {
            System.out.println("不可重复添加用户/修改用户名称");
            return;
        }
        onlinePlayer.put(player.getUsername(), player);
    }

    @Override
    public Player getPlayer(String username) {
        System.out.println("获取：" + username);
        return onlinePlayer.get(username);
    }

    @Override
    public void broadcast(String message) {
        System.out.println("广播消息：" + message);
        onlinePlayer.values().forEach(player -> player.write(message));
    }
}
