package com.example.springktabledemo.model;

public class User {
    private String id;
    private String dateTime;
    private Integer status;

    public User() {
    }

    public User(String id, String dateTime, Integer status) {
        this.id = id;
        this.dateTime = dateTime;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":\"" + id + "\"," +
                "\"dateTime\":\"" + dateTime + "\"," +
                "\"status\":" + status +
                "}";
    }
}
