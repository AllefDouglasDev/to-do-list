package com.example.allef.tad.model;

/**
 * Created by Allef on 31/05/2018.
 */

public class Notification
{
    private int id;
    private String dateToNotify;

    public Notification() { }

    public Notification(String dateToNotify) {
        this.dateToNotify = dateToNotify;
    }

    public Notification(int id, String dateToNotify) {
        this.id = id;
        this.dateToNotify = dateToNotify;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateToNotify() {
        return dateToNotify;
    }

    public void setDateToNotify(String dateToNotify) {
        this.dateToNotify = dateToNotify;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", dateToNotify='" + dateToNotify + '\'' +
                '}';
    }
}
