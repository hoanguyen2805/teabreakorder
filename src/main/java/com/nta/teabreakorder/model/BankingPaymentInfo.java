package com.nta.teabreakorder.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="banking_payment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankingPaymentInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String code;

    private String shortName;

    private String logo;

    private String bin;

    private String accountName;

    private String accountNo;

}
