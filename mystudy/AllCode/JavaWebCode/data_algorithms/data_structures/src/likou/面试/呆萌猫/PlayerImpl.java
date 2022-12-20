package likou.面试.呆萌猫;


public class PlayerImpl implements Player {
    private String userName;
    private boolean isOffline;


    public PlayerImpl(String userName) {
        this.userName = userName;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public void setUsername(String username) {
        if (userName == null || userName == "") {
            this.userName = userName;
        }
    }

    @Override
    public void write(String message) {
        System.out.println(userName+"收到消息:"+message);
    }


    @Override
    public boolean isOffline() {
        return false;
    }


    //方便测试使用
    public void setOffline(boolean offline) {
        this.isOffline=offline;
    }
}
