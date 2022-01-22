package com.shashankbhat.splitbill.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private String error;
    private String developerMessage;
    private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    private Date timestamp = new Date(System.currentTimeMillis());
}
