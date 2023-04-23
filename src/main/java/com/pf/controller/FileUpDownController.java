package com.pf.controller;

import com.pf.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(tags = "文件上传与下载")
@RestController
@RequestMapping("/file")
public class FileUpDownController {

    @Value("${filePath}")
    private String filePath;

    @Autowired
    private HttpServletResponse response;

    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public R upload(MultipartFile[] file) throws IOException {
        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        Map<String, String> data = new HashMap<>();
        int count = 1;
        for (MultipartFile f : file) {
            String filename = f.getOriginalFilename();
            filename = System.currentTimeMillis() + "_" + filename;
            f.transferTo(new File(filePath + filename));
            data.put("filename_" + count, filename);
            count++;
        }
        return R.success("上传成功", data);
    }

    @ApiOperation("文件下载")
    @PostMapping("/download")
    public void download(@RequestParam("filename") String filename) throws IOException {
        FileInputStream is = new FileInputStream(new File(filePath, filename));
        response.setContentType("text/plain;charset=UTF-8");
        response.setHeader("content-disposition", "attachment;fileName=" + filename);
        ServletOutputStream os = response.getOutputStream();
        IOUtils.copy(is, os);
        IOUtils.closeQuietly(is);
        IOUtils.closeQuietly(os);
    }
}
