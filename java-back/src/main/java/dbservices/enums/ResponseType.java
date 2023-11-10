package dbservices.enums;

import lombok.Getter;

@Getter
public enum ResponseType {
    OPERATION_SUCCESSFUL(200),
    NOT_FOUND(404);

    ResponseType(int code) {
        this.code = code;
    }

    int code;
}
