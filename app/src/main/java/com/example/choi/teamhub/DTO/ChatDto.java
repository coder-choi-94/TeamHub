package com.example.choi.teamhub.DTO;

public class ChatDto {

    private String writer;
    private String message;
    private String date;

    public String getWriter() {
        return writer;
    }

    public void setName(String writer) {
        this.writer = writer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
