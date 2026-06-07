package com.library.model;

/**
 * 图书分类实体类，对应数据库category表
 */
public class Category {
    private Integer categoryId;    // 分类ID（主键，自增）
    private String categoryName;   // 分类名称
    private Integer parentId;      // 父分类ID（自关联，NULL=一级分类）
    private String description;    // 分类描述

    // 空构造
    public Category() {}

    // 全字段构造
    public Category(Integer categoryId, String categoryName, Integer parentId, String description) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.parentId = parentId;
        this.description = description;
    }

    // Getter & Setter
    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", parentId=" + parentId +
                ", description='" + description + '\'' +
                '}';
    }
}