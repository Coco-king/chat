package top.codecrab.chat.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    private Boolean success;
    private String message;
    private Object result;

    public static Result success() {
        return new Result(true, "操作成功", null);
    }

    public static Result success(Object data) {
        return new Result(true, "操作成功", data);
    }

    public static Result success(Object data, String message) {
        return new Result(true, message, data);
    }

    public static Result fail() {
        return new Result(false, "操作失败", null);
    }

    public static Result fail(String message) {
        return new Result(false, message, null);
    }
}
