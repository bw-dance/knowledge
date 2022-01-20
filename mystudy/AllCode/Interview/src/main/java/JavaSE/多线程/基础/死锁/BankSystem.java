package JavaSE.多线程.基础.死锁;

/**
 * @Classname demo
 * @Description TODO
 * @Date 2022/1/18 11:56
 * @Created by zhq
 */
class Account {
    //账号
    private String actno;
    //余额
    private double balance;
    //表示操作：save 表示存储，take表示拿
    Object save;
    Object take;


    public Account(String actno, double balance, Object save, Object take) {
        this.actno = actno;
        this.balance = balance;
        this.save = save;
        this.take = take;
    }
    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    //存钱
    public void withDraw(double money) {
        double before = this.getBalance();
        double after = before - money;
        this.setBalance(after);
        System.out.println(Thread.currentThread().getName() + "取了" + money + "剩余余额" + after);
    }

    //取钱
    public void addDraw(double money) {
        double before = this.getBalance();
        double after = before + money;
        this.setBalance(after);
        System.out.println(Thread.currentThread().getName() + "存了" + money + "剩余余额" + after);
    }
}


class Person implements Runnable {
    Account account;


    public Person(Account account) {
        this.account = account;
    }

    @Override
    public void run() {
        if (Thread.currentThread().getName().equals("张三")) {
            System.out.println(account);
            synchronized (account.save) {
                account.addDraw(500);
                try {
                    //阻塞线程
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (account.take) {
                    account.withDraw(500);
                }
            }
        } else {
            synchronized (account.take) {
                try {
                    //阻塞线程
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                account.addDraw(500);
                synchronized (account.save) {
                    account.withDraw(500);
                }
            }
        }
    }
}

public class BankSystem {

    public static void main(String[] args) {
        Account account = new Account("actno_1", 1000, new Object(), new Object());
        Thread thread1 = new Thread(new Person(account));
        thread1.setName("张三");
        thread1.start();
        Thread thread2 = new Thread(new Person(account));
        thread2.setName("张三的妻子");
        thread2.start();
    }
}