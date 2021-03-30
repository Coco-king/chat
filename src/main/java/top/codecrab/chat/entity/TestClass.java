package top.codecrab.chat.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class TestClass {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String fromUserid;

    private String toUserid;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss.SSS", timezone = "GMT+8")
    private Date createtime;

    private String message;

    private Integer status;
}
