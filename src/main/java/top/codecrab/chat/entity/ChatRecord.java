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
@TableName("tb_chat_record")
public class ChatRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 用户id
     */
    private String userid;

    /**
     * 好友id
     */
    private String friendid;

    /**
     * 是否已读
     */
    private Integer hasRead;

    /**
     * 聊天时间
     */
    private LocalDate createtime;

    /**
     * 是否删除
     */
    private Integer hasDelete;

    /**
     * 消息
     */
    private String message;


}
