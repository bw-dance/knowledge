package cn.itcast.main;

import cn.itcast.dao.UserDao;
import cn.itcast.domain.User;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;

public class Test {
    public static void main(String[] args) {
    User user = new User();
        try {
           //设置对象
            BeanUtils.setProperty(user,"gender","male");
            System.out.println(user);      //    User{id=0, username='null', password='null', gender='null'}
            BeanUtils.setProperty(user,"genders","male");
            System.out.println(user);     //    User{id=0, username='null', password='null', gender='male'}
          //获取对象
            try {
                String genders = BeanUtils.getProperty(user, "genders");
                System.out.println(genders);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
