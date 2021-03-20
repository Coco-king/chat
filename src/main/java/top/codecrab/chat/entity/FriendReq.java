package top.codecrab.chat.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDate;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@TableName("tb_friend_req")
public class FriendReq implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 请求好友用户id
     */
    private String fromUserid;

    /**
     * 被请求好友用户id
     */
    private String toUserid;

    /**
     * 请求时间
     */
    private LocalDate createtime;

    /**
     * 发送的消息
     */
    private String message;

    /**
     * 是否已处理
     */
    private Integer status;


}
