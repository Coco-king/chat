package top.codecrab.chat.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author codecrab
 * @since 2021-03-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    @JsonIgnore
    private String password;

    /**
     * 头像（小尺寸）
     */
    private String picSmall;

    /**
     * 头像（正常尺寸）
     */
    private String picNormal;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 二维码
     */
    private String qrcode;

    /**
     * 手机端唯一ID
     */
    private String clientId;

    /**
     * 签名
     */
    private String sign;

    /**
     * 注册日期
     */
    private LocalDateTime createtime;

    /**
     * 绑定手机
     */
    private String phone;


}
