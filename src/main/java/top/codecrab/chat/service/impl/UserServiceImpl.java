package top.codecrab.chat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import top.codecrab.chat.bo.LoginUser;
import top.codecrab.chat.common.Result;
import top.codecrab.chat.entity.User;
import top.codecrab.chat.mapper.UserMapper;
import top.codecrab.chat.service.UserService;
import top.codecrab.chat.utils.CommonUtils;
import top.codecrab.chat.utils.FastDFSClient;
import top.codecrab.chat.utils.FileUtils;
import top.codecrab.chat.utils.QRCodeUtils;
import top.codecrab.chat.vo.AccountProfile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author codecrab
 * @since 2021-03-19
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Value("${chat.upload.file-path}")
    private String filePath;

    @Value("${fdfs.http-url}")
    private String httpUrl;

    @Value("${fdfs.suffix}")
    private String suffix;

    @Autowired
    private FastDFSClient fastDFSClient;

    @Autowired
    private QRCodeUtils qrCodeUtils;

    @Override
    @Transactional
    public Result register(LoginUser loginUser) {
        String username = loginUser.getUsername();
        User one = this.getOne(new QueryWrapper<User>()
                .eq("username", username));
        if (one != null) return Result.fail("用户名已存在");

        one = this.getOne(new QueryWrapper<User>()
                .eq("nickname", loginUser.getNickname()));
        if (one != null) return Result.fail("昵称已存在");

        try {
            File mkdir = new File(filePath);
            if (!mkdir.exists()) {
                boolean mkdirs = mkdir.mkdirs();
            }
            //生成二维码,获取临时储存路径
            String tempPath = filePath + username + ".png";
            //初始化二维码信息
            String qrCodeStr = "HiChat://" + username;
            //生成二维码
            qrCodeUtils.createQRCode(tempPath, qrCodeStr);
            //上传到fastDFS
            File file = new File(tempPath);
            String qrCodeUrl = httpUrl + fastDFSClient.uploadFile(file);
            FileUtils.deleteFile(tempPath);

            User user = new User();
            BeanUtil.copyProperties(loginUser, user);
            user.setPassword(SecureUtil.md5(loginUser.getPassword()));
            user.setPicSmall("image/default_user_pic.png");
            user.setPicNormal("image/default_user_pic.png");
            user.setQrcode(qrCodeUrl);
            user.setSign(CommonUtils.getSign());
            user.setCreatetime(LocalDateTime.now());
            user.setId(null);
            user.setPhone(null);
            this.save(user);

            AccountProfile profile = new AccountProfile();
            BeanUtil.copyProperties(user, profile);
            return Result.success(profile, "注册成功");
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail("注册失败");
        }
    }

    @Override
    public Result login(LoginUser loginUser) {
        User user = this.getOne(new QueryWrapper<User>()
                .eq("username", loginUser.getUsername()));
        if (user == null) return Result.fail("找不到用户");

        String Md5Pass = SecureUtil.md5(loginUser.getPassword());
        if (StrUtil.equals(Md5Pass, user.getPassword())) {
            return Result.success(user, "登陆成功");
        }

        return Result.fail("密码或用户名错误");
    }

    @Override
    @Transactional
    public Result upload(MultipartFile file, String userid) {
        User user = this.getById(userid);
        if (user == null) return Result.fail("找不到指定用户");
        try {
            String imgUrl = fastDFSClient.uploadFace(file);
            String[] split = imgUrl.split("\\.");
            String thumbImageUrl = httpUrl + split[0] + suffix + "." + split[1];

            user.setPicNormal(httpUrl + imgUrl);
            user.setPicSmall(thumbImageUrl);
            this.updateById(user);
            return Result.success(user);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail("上传失败");
        }
    }

    @Override
    @Transactional
    public Result updateNickname(AccountProfile profile) {
        User user = this.getById(profile.getId());
        if (user == null) return Result.fail("找不到指定用户");

        user.setNickname(profile.getNickname());
        this.updateById(user);
        return Result.success("修改成功");
    }
}
