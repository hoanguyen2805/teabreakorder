package com.nta.teabreakorder.payload.response;

import com.nta.teabreakorder.model.BankingPaymentInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String fullName;
    private List<BankingPaymentInfo> bankingPaymentInfoList;
}
