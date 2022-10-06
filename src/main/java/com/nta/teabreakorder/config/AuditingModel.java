package com.nta.teabreakorder.config;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nta.teabreakorder.common.Const;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;


@EqualsAndHashCode
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AuditingModel {


    @JsonProperty(value = "created_by")
    @Column(name = "created_by", updatable = false)
    @CreatedBy
    protected String createdBy;

    @JsonFormat(pattern = Const.DATETIME_PATTERN)
    @JsonProperty(value = "created_at")
    @Column(name = "created_at" ,updatable = false)
    @CreatedDate
    protected LocalDateTime createdAt;


    @JsonProperty(value = "modified_by")
    @Column(name = "modified_by")
    @LastModifiedBy
    protected String modifiedBy;

    @JsonFormat(pattern = Const.DATETIME_PATTERN)
    @JsonProperty(value = "modified_at")
    @Column(name = "modified_at")
    @LastModifiedDate
    protected LocalDateTime modifiedAt;


    @JsonProperty(value = "is_deleted")
    @Column(name = "is_deleted")
    protected boolean isDeleted = false;

    public AuditingModel() {
    }

    public AuditingModel(String createdBy, LocalDateTime createdAt, String modifiedBy, LocalDateTime modifiedAt, boolean isDeleted) {
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.modifiedBy = modifiedBy;
        this.modifiedAt = modifiedAt;
        this.isDeleted = isDeleted;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
