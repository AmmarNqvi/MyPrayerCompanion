package com.androidweardocs.wearablemessage;

public class ListViewItem {

    public int imageRes;
    public String text;

    public ListViewItem(int imageRes, String text) {
        this.imageRes = imageRes;
        this.text = text;
    }
    public ListViewItem( String text) {
        //this.imageRes = imageRes;
        this.text = text;
    }
}
