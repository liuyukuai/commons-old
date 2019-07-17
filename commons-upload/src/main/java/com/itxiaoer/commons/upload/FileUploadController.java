package com.itxiaoer.commons.upload;

import com.itxiaoer.commons.core.page.Response;
import com.itxiaoer.commons.upload.config.FileUploadProperties;
import com.itxiaoer.commons.upload.rule.IdRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.function.Function;

/**
 * 文件上传控制层
 *
 * @author : liuyk
 */
@Slf4j
@SuppressWarnings("unused")
@RestController
public class FileUploadController {

    @Resource
    private FileUploadProperties fileUploadProperties;

    @Resource
    private IdRule idRule;


    public Function<File, String> function() {
        return (file -> "");
    }

    @PostMapping("/upload")
    public Response<String> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Response.failure("文件不存在");
        }
        File filePath = new File(fileUploadProperties.getDir());
        if (!filePath.exists()) {
            boolean mkdirs = filePath.mkdirs();
            if (!mkdirs) {
                return Response.failure("创建文件夹失败");
            }
        }
        try {
            String id = idRule.id(file);
            file.transferTo(new File(filePath, id));
            return Response.ok(id);
        } catch (IOException e) {
            e.printStackTrace();
            return Response.failure("拷贝文件失败");
        }
    }
}
