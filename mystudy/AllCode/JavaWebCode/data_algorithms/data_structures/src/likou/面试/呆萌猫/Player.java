package likou.面试.呆萌猫;


/*
题目：实现Player和PlayerManager接口的功能。

要求：
1、Player对象以username为索引，且Player对象创建之后，username不会变化。
2、PlayerManager中的所有功能是线程安全的，可并发执行。
3、PlayerManager每隔一分钟会将isOffline() == true的Player对象删除。
4、编写针对PlayerManager功能的单元测试，确保PlayerManager的功能正确。
*/

public interface Player {
    /**
     * 用户名。
     */
    String getUsername();

    void setUsername(String username);

    /**
     * 向玩家发送消息。
     */
    void write(String message);

    /**
     * 玩家是否掉线。
     */
    boolean isOffline();

    void setOffline(boolean offline);
}