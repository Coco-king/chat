package top.codecrab.chat.netty;

import lombok.Data;
import top.codecrab.chat.entity.ChatRecord;

import java.io.Serializable;

@Data
public class Message implements Serializable {

    private Integer type;
    private ChatRecord chatRecord;
    private Object ext;

}
