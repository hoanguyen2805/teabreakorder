package com.nta.teabreakorder.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nta.teabreakorder.common.Const;
import com.nta.teabreakorder.enums.BankingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "banking_history")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankingHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "order_detail_id", referencedColumnName = "id")
    private OrderDetail orderDetail;

    private String userRequested;

    @JsonFormat(pattern = Const.DATETIME_PATTERN)
    private LocalDateTime timeRequested;

    private String userApproved;

    @JsonFormat(pattern = Const.DATETIME_PATTERN)
    private LocalDateTime timeApproved;

    private BankingStatus status;


}
