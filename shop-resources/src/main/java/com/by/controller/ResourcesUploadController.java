package com.by.controller;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
@RequestMapping("/ruController")
public class ResourcesUploadController {

    @Resource
    private FastFileStorageClient fastFileStorageClient;

    @Value("${fasthome}")
    private String dfsHome;

    @RequestMapping("/uploadPng")
    public String uploadPng(MultipartFile file) {
        try {
            String fileExtName = FilenameUtils.getExtension(file.getOriginalFilename());

            // 文件上传到FastDFS
            StorePath storePath = fastFileStorageClient.uploadImageAndCrtThumbImage(
                    file.getInputStream(), file.getSize(), fileExtName, null);

            // 获取文件上传路径
            String fullPath = storePath.getFullPath();

            return dfsHome + fullPath;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "上传图片失败";
    }

}
