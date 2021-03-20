package top.codecrab.chat.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class AccountProfile {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 手机端唯一ID
     */
    private String clientId;

    /**
     * 头像（小尺寸）
     */
    private String picSmall;
}
