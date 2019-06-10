package com.yuk.rpc;

import java.io.Serializable;

/**
 * Created by yuk on 2019/6/8.
 */
//实现序列化接口，这样对象才可以远程传输
public class RpcRequest implements Serializable{

    private String className;
    private String methodName;
    private Object[] parameters;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }
}
