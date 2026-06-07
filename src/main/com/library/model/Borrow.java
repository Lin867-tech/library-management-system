package com.library.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 借阅记录实体类，对应数据库borrow表
 */
public class Borrow {
    private Integer borrowId;      // 借阅ID（主键，自增）
    private Integer readerId;      // 读者ID（外键）
    private Integer bookId;        // 图书ID（外键）
    private Date borrowTime;       // 借阅时间
    private Date dueTime;          // 应还时间（默认借阅后30天）
    private Date returnTime;       // 实际归还时间（NULL=未归还）
    private Integer overdueStatus; // 逾期状态：0未逾期/1已逾期
    private BigDecimal fineAmount; // 罚款金额（默认0.00）

    // 空构造
    public Borrow() {}

    // 全字段构造
    public Borrow(Integer borrowId, Integer readerId, Integer bookId, Date borrowTime, Date dueTime, Date returnTime, Integer overdueStatus, BigDecimal fineAmount) {
        this.borrowId = borrowId;
        this.readerId = readerId;
        this.bookId = bookId;
        this.borrowTime = borrowTime;
        this.dueTime = dueTime;
        this.returnTime = returnTime;
        this.overdueStatus = overdueStatus;
        this.fineAmount = fineAmount;
    }

    // Getter & Setter
    public Integer getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(Integer borrowId) {
        this.borrowId = borrowId;
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

    public Date getBorrowTime() {
        return borrowTime;
    }

    public void setBorrowTime(Date borrowTime) {
        this.borrowTime = borrowTime;
    }

    public Date getDueTime() {
        return dueTime;
    }

    public void setDueTime(Date dueTime) {
        this.dueTime = dueTime;
    }

    public Date getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }

    public Integer getOverdueStatus() {
        return overdueStatus;
    }

    public void setOverdueStatus(Integer overdueStatus) {
        this.overdueStatus = overdueStatus;
    }

    public BigDecimal getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(BigDecimal fineAmount) {
        this.fineAmount = fineAmount;
    }

    @Override
    public String toString() {
        return "Borrow{" +
                "borrowId=" + borrowId +
                ", readerId=" + readerId +
                ", bookId=" + bookId +
                ", borrowTime=" + borrowTime +
                ", dueTime=" + dueTime +
                ", returnTime=" + returnTime +
                ", overdueStatus=" + overdueStatus +
                ", fineAmount=" + fineAmount +
                '}';
    }
}