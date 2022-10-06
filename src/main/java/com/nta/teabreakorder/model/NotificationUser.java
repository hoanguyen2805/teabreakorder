package com.nta.teabreakorder.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nta.teabreakorder.common.Const;
import com.nta.teabreakorder.payload.response.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "notification_user")
public class NotificationUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name="notification_id", referencedColumnName = "id")
    private Notification notification;

    @Column(name = "is_read")
    private boolean isRead = false;

    @Column(name = "product_name")
    @JsonProperty(value = "product_name")
    private String productName;

    @Column(name="photo")
    @JsonProperty(value="photo")
    private String photo;

    @JsonFormat(pattern = Const.DATETIME_PATTERN)
    @JsonProperty(value = "created_at")
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name="store_name")
    @JsonProperty(value="store_name")
    private String store_name;

    @Column(name="order_detail")
    @JsonProperty(value="order_detail")
    private Long order_detail;

    @Column(name="total")
    @JsonProperty(value="total")
    private String total;

    public NotificationUser(Long id, User user, Notification notification, boolean isRead, String productName, String photo, LocalDateTime createdAt, String store_name, Long order_detail, String total) {
        this.id = id;
        this.user = user;
        this.notification = notification;
        this.isRead = isRead;
        this.productName = productName;
        this.photo = photo;
        this.createdAt = createdAt;
        this.store_name = store_name;
        this.order_detail = order_detail;
        this.total = total;
    }


    public UserDTO getUser(){
        try{
            return  new UserDTO(user.getId(),user.getUsername(),user.getFullName(),user.getBankingPaymentInfoList());
        }catch (Exception e){
            return  null;
        }

    }

    public UserDTO getSender(){
        try{
            return  new UserDTO(sender.getId(),sender.getUsername(),sender.getFullName(),sender.getBankingPaymentInfoList());
        }catch (Exception e){
            return  null;
        }

    }
}
