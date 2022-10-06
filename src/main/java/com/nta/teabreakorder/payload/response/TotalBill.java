package com.nta.teabreakorder.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nta.teabreakorder.common.Const;

import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class TotalBill {

    private Long id;
    @JsonFormat(pattern = Const.DATETIME_PATTERN)
    private LocalDateTime createdAt;
    private String createdBy;
    private Boolean isDeleted;
    private String status;
    private String store;
    private String address;
    @JsonIgnore
    private String product;
    @JsonIgnore
    private Integer quantity;
    @JsonIgnore
    private BigDecimal total;
    @OneToMany
    private List<AmountProduct> totalProduct;
    private BigDecimal totalAll;
    @JsonIgnore
    private String photo;
    private String description;

    public TotalBill() {
    }

    public TotalBill(Long id, LocalDateTime createdAt, String createdBy, Boolean isDeleted, String status, String store, String address, String product, Integer quantity, BigDecimal total, String photo, String description) {
        this.id = id;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.isDeleted = isDeleted;
        this.status = status;
        this.store = store;
        this.address = address;
        this.product = product;
        this.quantity = quantity;
        this.total = total;
        this.photo = photo;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<AmountProduct> getTotalProduct() {
        return totalProduct;
    }

    public void setTotalProduct(List<AmountProduct> totalProduct) {
        this.totalProduct = totalProduct;
    }

    public BigDecimal getTotalAll() {
        return totalAll;
    }

    public void setTotalAll(BigDecimal totalAll) {
        this.totalAll = totalAll;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
