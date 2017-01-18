package com.example.jian.smarthome.Task;

/**
 * Created by jian on 2016/11/22.
 */

public class Bean {
    private float sun;
    private float tem;
    private float hum;
    private float pm25;

    public Bean(float sun, float tem, float hum, float pm25) {
        this.sun = sun;
        this.tem = tem;
        this.hum = hum;
        this.pm25 = pm25;
    }

    public float getSun() {
        return sun;
    }

    public void setSun(float sun) {
        this.sun = sun;
    }

    public float getTem() {
        return tem;
    }

    public void setTem(float tem) {
        this.tem = tem;
    }

    public float getHum() {
        return hum;
    }

    public void setHum(float hum) {
        this.hum = hum;
    }

    public float getPm25() {
        return pm25;
    }

    public void setPm25(float pm25) {
        this.pm25 = pm25;
    }
}
