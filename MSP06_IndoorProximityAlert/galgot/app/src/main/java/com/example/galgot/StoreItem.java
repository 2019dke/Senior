package com.example.galgot;

public class StoreItem {
    String name;
    //이미지도 추가할라면 하기

    public StoreItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "StoreItem{" +
                "name='" + name + '\'' +
                '}';
    }
}
