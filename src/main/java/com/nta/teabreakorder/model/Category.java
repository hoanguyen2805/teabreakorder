package com.nta.teabreakorder.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.nta.teabreakorder.config.AuditingModel;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "categories")
public class Category extends AuditingModel {
    @Id
    @JsonProperty("dish_type_id")
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dish_type_name")
    @JsonProperty("dish_type_name")
    private String dishTypeName;

    @Column(name = "is_group_discount")
    @JsonProperty("is_group_discount")
    private boolean isGroupDiscount;

    @JsonProperty(value = "dishes")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    List<Product> productList;


    public Category() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDishTypeName() {
        return dishTypeName;
    }

    public void setDishTypeName(String dishTypeName) {
        this.dishTypeName = dishTypeName;
    }

    public boolean isGroupDiscount() {
        return isGroupDiscount;
    }

    public void setGroupDiscount(boolean groupDiscount) {
        isGroupDiscount = groupDiscount;
    }


    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}
