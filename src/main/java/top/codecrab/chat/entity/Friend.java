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
@TableName("tb_friend")
public class Friend implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 用户id
     */
    private String userid;

    /**
     * 好友id
     */
    private String friendsId;

    /**
     * 备注
     */
    private String comments;

    /**
     * 添加好友日期
     */
    private LocalDate createtime;


}
