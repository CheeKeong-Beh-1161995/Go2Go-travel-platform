package com.example.entity;

import java.math.BigDecimal;

public class Orders {
    private Integer id;
    private String name;
    private String orderNo;
    private String tourismImg;
    private BigDecimal tourismPrice;
    private Integer tourismId;
    private Integer num;
    private BigDecimal total;
    private Integer userId;
    private String time;
    private String payNo;
    private String payTime;
    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getTourismImg() {
        return tourismImg;
    }

    public void setTourismImg(String tourismImg) {
        this.tourismImg = tourismImg;
    }

    public BigDecimal getTourismPrice() {
        return tourismPrice;
    }

    public void setTourismPrice(BigDecimal tourismPrice) {
        this.tourismPrice = tourismPrice;
    }

    public Integer getTourismId() {
        return tourismId;
    }

    public void setTourismId(Integer tourismId) {
        this.tourismId = tourismId;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPayNo() {
        return payNo;
    }

    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
