package top.codecrab.chat.bo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class LoginUser {

    private Long id;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Length(min = 4, max = 16, message = "用户名长度在4-16个字符之间")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Length(min = 6, max = 16, message = "密码长度在4-16个字符之间")
    private String password;

    /**
     * 昵称
     */
    @NotBlank(message = "昵称不能为空")
    private String nickname;

    /**
     * 手机端唯一ID
     */
    private String clientId;
}
