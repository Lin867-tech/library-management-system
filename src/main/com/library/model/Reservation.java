package com.library.model;

import java.util.Date;

/**
 * 预约记录实体类，对应数据库reservation表
 */
public class Reservation {
    private Integer reserveId;     // 预约ID（主键，自增）
    private Integer readerId;      // 读者ID（外键）
    private Integer bookId;        // 图书ID（外键）
    private Date reserveTime;      // 预约时间
    private Integer reserveStatus; // 预约状态：0待处理/1已生效/2已取消

    // 空构造
    public Reservation() {}

    // 全字段构造
    public Reservation(Integer reserveId, Integer readerId, Integer bookId, Date reserveTime, Integer reserveStatus) {
        this.reserveId = reserveId;
        this.readerId = readerId;
        this.bookId = bookId;
        this.reserveTime = reserveTime;
        this.reserveStatus = reserveStatus;
    }

    // Getter & Setter
    public Integer getReserveId() {
        return reserveId;
    }

    public void setReserveId(Integer reserveId) {
        this.reserveId = reserveId;
    }

    public Integer getReaderId() {
        return readerId;
    }

    public void setReaderId(Integer readerId) {
        this.readerId = readerId;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public Date getReserveTime() {
        return reserveTime;
    }

    public void setReserveTime(Date reserveTime) {
        this.reserveTime = reserveTime;
    }

    public Integer getReserveStatus() {
        return reserveStatus;
    }

    public void setReserveStatus(Integer reserveStatus) {
        this.reserveStatus = reserveStatus;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "reserveId=" + reserveId +
                ", readerId=" + readerId +
                ", bookId=" + bookId +
                ", reserveTime=" + reserveTime +
                ", reserveStatus=" + reserveStatus +
                '}';
    }
}