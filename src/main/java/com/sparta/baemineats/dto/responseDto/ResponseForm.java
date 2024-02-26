package com.sparta.baemineats.dto.responseDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseForm {

    private int httpStatus;
    private String message;
    private Object data;
}
