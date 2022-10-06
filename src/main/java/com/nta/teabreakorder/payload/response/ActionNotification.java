package com.nta.teabreakorder.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nta.teabreakorder.common.Const;
import com.nta.teabreakorder.enums.Message;
import com.nta.teabreakorder.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ActionNotification {

    private Long notification_id;

    private Message message;

    private Long order_id;

    private Long notification_user_id;

    private Boolean is_read;

    private Long user_id;

    private String product_name;

    private String photo;

    private String created_at;

    private String store_name;

    private Long order_detail;

    private String total;

    private UserDTO user;

    private UserDTO sender;

    public ActionNotification(Long notification_id, Message message, Long order_id, Long notification_user_id, Boolean is_read, Long user_id, String product_name, String photo, String created_at, String store_name, Long order_detail, String total) {
        this.notification_id = notification_id;
        this.message = message;
        this.order_id = order_id;
        this.notification_user_id = notification_user_id;
        this.is_read = is_read;
        this.user_id = user_id;
        this.product_name = product_name;
        this.photo = photo;
        this.created_at = created_at;
        this.store_name = store_name;
        this.order_detail = order_detail;
        this.total = total;
    }
}
