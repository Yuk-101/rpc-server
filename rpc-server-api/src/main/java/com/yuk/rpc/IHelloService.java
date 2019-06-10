package com.yuk.rpc;

/**
 * Created by yuk on 2019/6/8.
 */
public interface IHelloService {

    public String sayHello(String content);

    //保存用户
    public String saveUser(User user);
}
