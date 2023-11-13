package com.hello.dbservices.enums;

import lombok.Getter;

@Getter
public enum ResponseType {
    OPERATION_SUCCESSFUL(200),
    NOT_FOUND(404),
    UNAUTHORIZED(401);

    ResponseType(int code) {
        this.code = code;
    }

    int code;
}
