/*
 * Student ID: 2024EB01570
 * Course: Programming in Mobile Devices - Staff Graded Assignment 2
 * Java Data Model for Campus Notice Items
 */
package com.campus.noticeboard.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Notice {
    private String id;
    private String title;
    private String description;
    private String createdBy;
    private long timestamp;

    // Default constructor required for Firebase DataSnapshot.getValue(Notice.class)
    public Notice() {
    }

    public Notice(String id, String title, String description, String createdBy, long timestamp) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdBy = createdBy;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
