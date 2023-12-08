package com.example.myapplication.Model;

public class Observation {
    private int ob_id;
    private int ob_hike_id;
    private String ob_name;
    private String ob_time;
    private String ob_image;

    public int getOb_id() {
        return ob_id;
    }

    public void setOb_id(int ob_id) {
        this.ob_id = ob_id;
    }

    public int getOb_hike_id() {
        return ob_hike_id;
    }

    public void setOb_hike_id(int ob_hike_id) {
        this.ob_hike_id = ob_hike_id;
    }

    public String getOb_name() {
        return ob_name;
    }

    public void setOb_name(String ob_name) {
        this.ob_name = ob_name;
    }

    public String getOb_time() {
        return ob_time;
    }

    public void setOb_time(String ob_time) {
        this.ob_time = ob_time;
    }

    public String getOb_image() {
        return ob_image;
    }

    public void setOb_image(String ob_image) {this.ob_image = ob_image;}
}
