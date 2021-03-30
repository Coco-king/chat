package top.codecrab.chat;

import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.codecrab.chat.entity.TestClass;
import top.codecrab.chat.utils.JSONUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
class ChatApplicationTests {

    @Test
    void contextLoads() {

        TestClass req = new TestClass();
        req.setId(1111111111L);
        req.setFromUserid("");
        req.setToUserid("");
//        req.setCreatetime(LocalDateTime.now());
        req.setMessage("");
        req.setStatus(0);


        String jsonStr1 = JSONUtil.toJsonStr(req);
        String jsonStr2 = JSONUtils.toJsonStr(req);
        System.out.println(jsonStr1);
        System.out.println(jsonStr2);
        TestClass map = JSONUtil.toBean(jsonStr1, TestClass.class);
        TestClass map1 = JSONUtils.toBean(jsonStr2, TestClass.class);
        System.out.println(map);
        System.out.println(map1);
    }

    @Test
    void test() {
        List<TestClass> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            TestClass req = new TestClass();
            req.setId(1111111111L);
            req.setFromUserid("");
            req.setToUserid("");
//            req.setCreatetime(LocalDateTime.now());
            req.setMessage("");
            req.setStatus(0);
            list.add(req);
        }

        String jsonStr1 = JSONUtil.toJsonStr(list);
        String jsonStr2 = JSONUtils.toJsonStr(list);
        System.out.println(jsonStr1);
        System.out.println(jsonStr2);
        List<TestClass> list1 = JSONUtil.toList(jsonStr1, TestClass.class);
        List<TestClass> list2 = JSONUtils.toList(jsonStr2, TestClass.class);
        System.out.println(list1);
        System.out.println(list2);
    }
}
