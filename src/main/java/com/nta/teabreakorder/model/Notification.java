package com.nta.teabreakorder.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nta.teabreakorder.enums.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message")
    @JsonProperty("message")
    @Enumerated(EnumType.STRING)
    private Message message;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    @OneToMany(cascade = CascadeType.MERGE, orphanRemoval = true)
    @JoinColumn(name = "notification_id", referencedColumnName = "id")
    private List<NotificationUser> notificationUsers = new ArrayList<>();

    @PreRemove
    void beforeRemove() {
        this.getNotificationUsers().forEach(element -> {
            element.setNotification(null);
        });
    }
}
