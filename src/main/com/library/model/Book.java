package com.library.model;

import java.util.Date;

/**
 * 图书实体类，对应数据库book表
 */
public class Book {
    private Integer bookId;        // 图书ID（主键，自增）
    private String isbn;           // ISBN号（唯一）
    private String bookName;       // 图书名称
    private String author;         // 作者
    private String publisher;      // 出版社
    private Date publishDate;      // 出版日期
    private Integer categoryId;    // 所属分类ID（外键）
    private Integer stock;         // 库存数量
    private Integer status;        // 状态：0在馆/1借出/2下架

    // 空构造
    public Book() {}

    // 全字段构造
    public Book(Integer bookId, String isbn, String bookName, String author, String publisher, Date publishDate, Integer categoryId, Integer stock, Integer status) {
        this.bookId = bookId;
        this.isbn = isbn;
        this.bookName = bookName;
        this.author = author;
        this.publisher = publisher;
        this.publishDate = publishDate;
        this.categoryId = categoryId;
        this.stock = stock;
        this.status = status;
    }

    // Getter & Setter
    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", isbn='" + isbn + '\'' +
                ", bookName='" + bookName + '\'' +
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", publishDate=" + publishDate +
                ", categoryId=" + categoryId +
                ", stock=" + stock +
                ", status=" + status +
                '}';
    }
}