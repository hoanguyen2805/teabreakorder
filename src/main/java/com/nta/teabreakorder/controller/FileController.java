package com.nta.teabreakorder.controller;

import com.nta.teabreakorder.service.impl.FilesStorageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class FileController {

    private static final String AVATAR_PATH = "avatar/";
    private static final String QRCODE_PATH = "qrcode/";

    @Autowired
    private FilesStorageServiceImpl filesStorageService;

    @PostMapping("/avatar")
    public ResponseEntity uploadAvatar(@RequestParam MultipartFile file) throws Exception {
        String url = filesStorageService.save(file, AVATAR_PATH);
        return ResponseEntity.ok(url);
    }

    @PostMapping("/qrcode")
    public ResponseEntity uploadQrCode(@RequestParam MultipartFile file) throws Exception {
        String url = filesStorageService.save(file, QRCODE_PATH);
        return ResponseEntity.ok(url);
    }

    @DeleteMapping("")
    public ResponseEntity removeFile(@RequestParam String url) throws Exception {
        filesStorageService.delete(url);
        return ResponseEntity.ok(url);
    }

}
