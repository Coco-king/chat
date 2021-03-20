package top.codecrab.chat.utils;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.exception.FdfsUnsupportStorePathException;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class FastDFSClient {

    @Autowired
    private FastFileStorageClient storageClient;

    /**
     * 上传文件
     *
     * @param file 文件对象
     * @return 文件访问地址
     */
    public String uploadFile(MultipartFile file) throws IOException {
        StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(),
                FilenameUtils.getExtension(file.getOriginalFilename()), null);
        return storePath.getFullPath();
    }

    /**
     * 上传文件，带缩略图
     *
     * @param file 文件对象
     * @return 文件访问地址
     */
    public String uploadFileWithThumbImage(MultipartFile file) throws IOException {
        StorePath storePath = storageClient.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(),
                FilenameUtils.getExtension(file.getOriginalFilename()), null);
        return storePath.getFullPath();
    }

    /**
     * 上传文件
     *
     * @param file 文件对象
     * @return 文件访问地址
     */
    public String uploadFile(File file) throws IOException {
        StorePath storePath = storageClient.uploadFile(new FileInputStream(file), FileUtils.sizeOf(file),
                FilenameUtils.getExtension(file.getName()), null);
        return storePath.getFullPath();
    }

    /**
     * 上传二维码
     */
    public String uploadQRCode(MultipartFile file) throws IOException {
        StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(),
                "png", null);
        return storePath.getFullPath();
    }

    /**
     * 带缩略图的头像
     */
    public String uploadFace(MultipartFile file) throws IOException {
        StorePath storePath = storageClient.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(),
                "png", null);
        return storePath.getFullPath();
    }

    /**
     * 带缩略图的base64类型文件
     */
    public String uploadBase64(MultipartFile file) throws IOException {
        StorePath storePath = storageClient.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(),
                "png", null);
        return storePath.getFullPath();
    }

    /**
     * 将一段字符串生成一个文件上传
     *
     * @param content 文件内容
     */
    public String uploadFile(String content, String fileExtension) {
        byte[] buff = content.getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream stream = new ByteArrayInputStream(buff);
        StorePath storePath = storageClient.uploadFile(stream, buff.length, fileExtension, null);
        return storePath.getFullPath();
    }

    /**
     * 删除文件
     *
     * @param fileUrl 文件访问地址
     */
    public void deleteFile(String fileUrl) {
        if (StringUtils.isEmpty(fileUrl)) return;
        try {
            StorePath storePath = StorePath.parseFromUrl(fileUrl);
            storageClient.deleteFile(storePath.getGroup(), storePath.getPath());
        } catch (FdfsUnsupportStorePathException e) {
            log.error("删除文件出错 文件访问地址为 ======> {}  错误信息 ====> {}", fileUrl, e.getMessage());
        }
    }
}
