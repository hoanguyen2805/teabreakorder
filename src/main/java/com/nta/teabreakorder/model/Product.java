package com.nta.teabreakorder.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nta.teabreakorder.config.AuditingModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product extends AuditingModel {

    @Id
    @Column(name = "id")
    @JsonProperty(value = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "name")
    @JsonProperty(value = "name")
    private String name;


    @Column(name = "description")
    @JsonProperty(value = "description")
    private String description;


    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty("discount_price")
    @JoinColumn(name = "price_discount_id", referencedColumnName = "id")
    private Price discountPrice;


    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty("price")
    @JoinColumn(name = "price_id", referencedColumnName = "id")
    private Price price;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty("photos")
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private List<Photo> photoList;

    @Column(name = "is_available")
    @JsonProperty("is_available")
    private boolean isAvailable = true;

    @Column(name = "total_like")
    @JsonProperty("total_like")
    private String totalLike;


    public List<Photo> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<Photo> photoList) {
        if (photoList != null && photoList.size() > 0) {
            if (this.photoList == null) {
                this.photoList = new ArrayList<>();
            }
            this.photoList.add(photoList.get(0));
        } else {
            this.photoList = photoList;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Price getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Price discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getTotalLike() {
        return totalLike;
    }

    public void setTotalLike(String totalLike) {
        this.totalLike = totalLike;
    }
}
