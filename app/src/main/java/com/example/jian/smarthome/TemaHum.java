package com.example.jian.smarthome;

/**
 * Created by jian on 2016/12/19.
 */
public class TemaHum {
    private Integer Tem;
    private Integer Hum;

    public TemaHum(Integer tem, Integer hum) {
        Tem = tem;
        Hum = hum;
    }
    public Integer getTem() {
        return Tem;
    }

    public Integer getHum() {
        return Hum;
    }
}
