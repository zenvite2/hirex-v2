package com.ptit.hirex.exception;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiException extends RuntimeException {
    private int code;
    private String message;
}
