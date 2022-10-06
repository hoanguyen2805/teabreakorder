package com.nta.teabreakorder.payload.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderDetailCount {
    private int activated;
    private int unactivated;
    private int done;
    private int reject;
}
