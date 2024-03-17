package com.example.couchpotatosplan.month;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ExcludeEvent {
    private Long id;
    private int start_hour;
    private int start_min;
    private int end_hour;
    private int end_min;
    private String content;

    public ExcludeEvent() {

    }

    public ExcludeEvent(Long id, int start_hour, int start_min, int end_hour, int end_min, String content) {
        this.id = id;
        this.start_hour = start_hour;
        this.start_min = start_min;
        this.end_hour = end_hour;
        this.end_min = end_min;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getStart_hour() {
        return start_hour;
    }

    public void setStart_hour(int start_hour) {
        this.start_hour = start_hour;
    }

    public int getStart_min() {
        return start_min;
    }

    public void setStart_min(int start_min) {
        this.start_min = start_min;
    }

    public int getEnd_hour() {
        return end_hour;
    }

    public void setEnd_hour(int end_hour) {
        this.end_hour = end_hour;
    }

    public int getEnd_min() {
        return end_min;
    }

    public void setEnd_min(int end_min) {
        this.end_min = end_min;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}