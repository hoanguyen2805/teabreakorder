package com.nta.teabreakorder.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ActionEvent {
    private String username;
    private String eventName;
    private String msg;
}
