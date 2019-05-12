package com.example.rikkeisoft.data.model;

import android.graphics.Color;

import java.util.Date;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Note extends RealmObject {
    @PrimaryKey
    private int id;
    private String title;
    private String content;
    private boolean isAlarm;
    private String day;
    private String hour;
    private int color;
    private Date createDate;
    private RealmList<String> urls;

    public Note(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Note() {
        isAlarm = false;
        color = Color.WHITE;
    }

    public Note(Note note) {
        this.id = note.getId();
        this.title = note.getTitle();
        this.content = note.getContent();
        this.isAlarm = note.isAlarm;
        this.day = note.getDay();
        this.hour = note.getHour();
        this.color = note.getColor();
        this.createDate = note.getCreateDate();
        this.urls = note.getUrls();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isAlarm() {
        return isAlarm;
    }

    public void setAlarm(boolean alarm) {
        isAlarm = alarm;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public RealmList<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        RealmList<String> urlsRealm = new RealmList<>();
        urlsRealm.addAll(urls);
        this.urls = urlsRealm;
    }
}
