package top.codecrab.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import top.codecrab.chat.service.UserService;
import top.codecrab.chat.utils.ValidationUtil;

public class BaseController {
    @Autowired
    protected UserService userService;
    @Autowired
    protected ValidationUtil validationUtil;
}
