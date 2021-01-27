package com.example.thevision2.Messages.Model;


import java.util.Date;

public class SMSModel {
    private String title;
    private Date date;
    private String messageBody;

    public SMSModel(String title, Date date, String messageBody) {
        this.title = title;
        this.date = date;
        this.messageBody = messageBody;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }
}
