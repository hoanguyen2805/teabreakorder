package com.nta.teabreakorder.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.nta.teabreakorder.config.AuditingModel;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@SqlResultSetMapping(
        name = "StoreMapping",
        classes = {
                @ConstructorResult(
                        targetClass = com.nta.teabreakorder.model.Store.class,
                        columns = {
                                @ColumnResult(name = "id", type = Long.class),
                                @ColumnResult(name = "url", type = String.class),
                                @ColumnResult(name = "name", type = String.class),
                                @ColumnResult(name = "address", type = String.class),
                                @ColumnResult(name = "created_by", type = String.class),
                                @ColumnResult(name = "modified_by", type = String.class),
                                @ColumnResult(name = "created_at", type = LocalDateTime.class),
                                @ColumnResult(name = "modified_at", type = LocalDateTime.class),
                                @ColumnResult(name = "is_deleted", type = Boolean.class),

                        }),

        })
@Entity
@Table(name = "stores")
public class Store extends AuditingModel {

    @Id
    @JsonProperty("id")
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("url")
    @Column(name = "url")
    private String url;

    @JsonProperty("name")
    @Column(name = "name")
    private String name;

    @JsonProperty("address")
    @Column(name = "address")
    private String address;

    @JsonProperty("menu_infos")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "store_id", referencedColumnName = "id")
    private List<Category> categoryList;

    @Transient
    @JsonProperty("photos")
    private List<Photo> photos;

    @Column(name = "img")
    private String img;


    public Store() {
    }

    public Store(String createdBy, LocalDateTime createdAt, String modifiedBy, LocalDateTime modifiedAt, boolean isDeleted, Long id, String name) {
        super(createdBy, createdAt, modifiedBy, modifiedAt, isDeleted);
        this.id = id;
        this.name = name;
    }

    public Store(Long id, String url, String name, String address, String createdBy, String modifiedBy, LocalDateTime createdAt, LocalDateTime modifiedAt, boolean status) {
        super(createdBy, createdAt, modifiedBy, modifiedAt, status);
        this.id = id;
        this.url = url;
        this.name = name;
        this.address = address;
    }

    public Store(Long storeId, String storeName,String img, String address) {
        this.id = storeId;
        this.name = storeName;
        this.img = img;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) { ;
        if (photos != null && photos.size() > 0) {
            this.img = photos.get(photos.size() - 1).getValue();
        }
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }


}
