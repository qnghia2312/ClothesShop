package com.example.clothes.Model;

public class discountOnOrder {
    private String id;
    private String name;
    private String timeStart;
    private String timeEnd;
    private  int percent;
    private  String des;
    private int condition;

    public discountOnOrder() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }

    public discountOnOrder(String id, String name, String timeStart, String timeEnd, int percent, String des, int condition) {
        this.id = id;
        this.name = name;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.percent = percent;
        this.des = des;
        this.condition = condition;
    }
    @Override
    public String toString() {
        return "discountOnOrder{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", timeStart='" + timeStart + '\'' +
                ", timeEnd='" + timeEnd + '\'' +
                ", percent=" + percent +
                ", des='" + des + '\'' +
                ", condition=" + condition +
                '}';
    }
}
