package com.yuk.rpc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * Created by yuk on 2019/6/8.
 */
public class ProcessorHandler implements Runnable{

    private Object service;
    private Socket socket;


    public ProcessorHandler(Object service, Socket socket) {
        this.service = service;
        this.socket = socket;
    }

    @Override
    public void run() {
        ObjectInputStream objectInputStream = null;
        ObjectOutputStream objectOutputStream = null;

        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            //输入流中应该包含：请求类、方法、参数
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();//反序列化过程
            Object result = invoke(rpcRequest); //反射调用本地服务

            //写出
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(result);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }finally {
            if(objectInputStream != null){
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(objectOutputStream != null){
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Object invoke(RpcRequest rpcRequest) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //通过反射调用
        Object[] args = rpcRequest.getParameters();//拿到客户端请求参数
        Class<?> [] types = new Class[args.length];//获得每个参数的类型
        for(int i=0;i<args.length;i++){
            types[i] = args[i].getClass();
        }
        Class clazz = Class.forName(rpcRequest.getClassName());//根据请求的类去加载
        Method method = clazz.getMethod(rpcRequest.getMethodName(),types);//通过请求的类找到类中的方法
        Object result = method.invoke(service, args);//进行反射调用
        return result;
    }
}
