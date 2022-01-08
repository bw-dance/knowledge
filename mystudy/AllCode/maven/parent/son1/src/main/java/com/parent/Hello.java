package com.parent;

import cn.hutool.core.util.ObjectUtil;
import com.son1.User;

public class Hello {
    public static void main(String[] args) {
        User user = new User();
        System.out.println(ObjectUtil.isNotNull(user));
    }
}
