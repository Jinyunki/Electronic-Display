package com.example.display.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "table_name")
public class LikeData implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "text")
    private String text;

    @ColumnInfo(name = "textColor")
    private int textColor;

    @ColumnInfo(name = "backColor")
    private int backColor;

    public LikeData() {
    }

    public LikeData(int id, String text, int textColor, int backColor) {
        this.id = id;
        this.text = text;
        this.textColor = textColor;
        this.backColor = backColor;
    }

    public LikeData(String text, int textColor, int backColor) {
        this.text = text;
        this.textColor = textColor;
        this.backColor = backColor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getBackColor() {
        return backColor;
    }

    public void setBackColor(int backColor) {
        this.backColor = backColor;
    }
}