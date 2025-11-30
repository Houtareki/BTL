package com.example.demo.Model.ResponseModel;

public class CustomResponse<T> {
    private String status;
    private String message;
    private CustomData<T> data;
    
    public CustomResponse(String status, String message, CustomData<T> data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public CustomResponse() {
        this.status = null;
        this.message = null;
        this.data = null;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CustomData<T> getData() {
        return data;
    }

    public void setData(CustomData<T> data) {
        this.data = data;
    }

    

}
