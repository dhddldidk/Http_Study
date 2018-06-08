package kr.or.dgit.it.http_study.recyclerview;

import android.view.View;

import java.io.Serializable;

public class Item implements Serializable{
    private String title;
    private String detail;
    private int img;
    private int visible;
    private boolean checked;

    public Item() {
    }

    public Item(String title, String detail) {
        this.title = title;
        this.detail = detail;
    }

    public Item(String title, String detail, int img) {
        this.title = title;
        this.detail = detail;
        this.img = img;
        this.visible = View.GONE;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
