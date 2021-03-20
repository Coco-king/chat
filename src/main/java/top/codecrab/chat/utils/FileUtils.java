package top.codecrab.chat.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
@Component
public class FileUtils {

    /**
     * 根据url拿取file
     *
     * @param suffix 文件后缀名
     */
    public static File createFileByUrl(String url, String suffix) {
        byte[] byteFile = getImageFromNetByUrl(url);
        if (byteFile != null) {
            return getFileFromBytes(byteFile, suffix);
        } else {
            return null;
        }
    }

    /**
     * 根据地址获得数据的字节流
     *
     * @param strUrl 网络连接地址
     */
    private static byte[] getImageFromNetByUrl(String strUrl) {
        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();// 通过输入流获取图片数据
            return readInputStream(inStream);// 得到图片的二进制数据
        } catch (Exception e) {
            e.printStackTrace();
            log.error("根据地址获得数据的字节流失败 URL ====> {}", strUrl);
        }
        return null;
    }

    /**
     * 从输入流中获取数据
     *
     * @param inStream 输入流
     */
    private static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

    // 创建临时文件
    private static File getFileFromBytes(byte[] b, String suffix) {
        BufferedOutputStream stream = null;
        File file = null;
        try {
            file = File.createTempFile("pattern", "." + suffix);
            log.info("临时文件位置：{}", file.getCanonicalPath());
            stream = new BufferedOutputStream(new FileOutputStream(file));
            stream.write(b);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("临时文件写入失败");
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("临时文件写入失败,关闭流失败===> {}", stream.toString());
                }
            }
        }
        return file;
    }

    public static MultipartFile createImg(String url) {
        try {
            // File转换成MultipartFile
            File file = FileUtils.createFileByUrl(url, "jpg");
            if (file == null) {
                log.error("File转换成MultipartFile失败 URL ===> {}", url);
                return null;
            }
            FileInputStream inputStream = new FileInputStream(file);
            return new MockMultipartFile(file.getName(), inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("File转换成MultipartFile失败 URL ===> {}", url);
            return null;
        }
    }

    public static MultipartFile fileToMultipart(String filePath) {
        try {
            // File转换成MultipartFile
            File file = new File(filePath);
            FileInputStream inputStream = new FileInputStream(file);
            return new MockMultipartFile(file.getName(), "png", "image/png", inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        FileUtils.createFileByUrl("http://122.152.205.72:88/group1/M00/00/01/CpoxxFr7oIaAZ0rOAAC0d3GKDio580.png", "png");
        FileUtils.createImg("http://122.152.205.72:88/group1/M00/00/01/CpoxxFr7oIaAZ0rOAAC0d3GKDio580.png");
    }

    public static boolean base64ToFile(String filePath, String base64Data) throws Exception {
        String data;

        if (base64Data == null || "".equals(base64Data)) {
            return false;
        } else {
            String[] d = base64Data.split("base64,");
            if (d.length == 2) {
                data = d[1];
            } else {
                return false;
            }
        }

        // 因为BASE64Decoder的jar问题，此处使用spring框架提供的工具包
        byte[] bs = Base64Utils.decodeFromString(data);
        // 使用apache提供的工具类操作流
        org.apache.commons.io.FileUtils.writeByteArrayToFile(new File(filePath), bs);
        return true;
    }

    public static boolean deleteFile(String path) {
        boolean result = false;
        File file = new File(path);
        if (file.isFile() && file.exists()) {
            int tryCount = 0;
            while (!result && tryCount++ < 10) {
                System.gc();
                result = file.delete();
            }
        }
        return result;
    }
}
