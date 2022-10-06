package com.nta.teabreakorder.payload.response;

import com.nta.teabreakorder.enums.Status;
import com.nta.teabreakorder.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class OrderDTO {

    private Long id ;

    private Status status;

    private LocalDateTime timeRemaining;

}
