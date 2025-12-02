package com.zero.blog.service;

import com.zero.blog.entity.UploadFileList;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;


public interface UploadFileListService extends IService<UploadFileList> {

    String getUploadFileUrl(MultipartFile file);
}
