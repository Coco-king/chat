package top.codecrab.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import top.codecrab.chat.service.ChatRecordService;
import top.codecrab.chat.service.FriendReqService;
import top.codecrab.chat.service.UserService;
import top.codecrab.chat.utils.ValidationUtil;

public class BaseController {
    @Autowired
    protected UserService userService;
    @Autowired
    protected FriendReqService friendReqService;
    @Autowired
    protected ChatRecordService chatRecordService;
    @Autowired
    protected ValidationUtil validationUtil;
}
