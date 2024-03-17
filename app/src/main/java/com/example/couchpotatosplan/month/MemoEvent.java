package com.example.couchpotatosplan.month;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class MemoEvent {
    private Long id;
    private String title;
    private String content;
    private String time;

    public MemoEvent() {

    }

    public MemoEvent(Long id, String title, String content, String time) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title){ this.title = title;}

    public String getTitle(){ return title;}

    public String getTime(){ return time;}

    public void setTime(String time){this.time = time;}

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}