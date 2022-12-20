package likou.面试.呆萌猫;

import java.util.concurrent.ConcurrentLinkedQueue;

public interface PlayerManager {
    ConcurrentLinkedQueue<String> messageQueue = new ConcurrentLinkedQueue<>();

    /**
     * 增加一个玩家对象。
     */
    void addPlayer(Player player);

    /**
     * 根据用户名获取玩家对象。
     */
    Player getPlayer(String username);

    /**
     * 向系统中的所有玩家广播一条消息。
     */
    void broadcast(String message);
}

