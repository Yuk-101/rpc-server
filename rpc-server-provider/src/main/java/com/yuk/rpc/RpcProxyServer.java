package com.yuk.rpc;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yuk on 2019/6/8.
 */
public class RpcProxyServer {

    ExecutorService executorService = Executors.newCachedThreadPool();//可缓存的线程池

    public void publisher(Object service,int port){
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(port);
            while(true){//不断接受请求
                Socket socket = serverSocket.accept();
                //每一个socket 交给processorHandler 处理
                ProcessorHandler handler = new ProcessorHandler(service, socket);
                executorService.execute(handler);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(serverSocket != null){
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
