package com.example.demo.Model.ResponseModel;


public class CustomData<T> {
    private T data;

    public CustomData(T data) {
        this.data = data;
    }

    public CustomData() {
        this.data = null;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    
}
