package com.nta.teabreakorder.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FilesStorageServiceImpl {
    public static final String UPLOAD_PATH = "uploads/";


    public void init(String path) {
        try {
            final Path root = Paths.get(UPLOAD_PATH + path);
            if (!Files.isDirectory(root )) {
                Files.createDirectory(root);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    public String save(MultipartFile file, String path) {
        try {
            init(path);
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            long date = new Date().getTime();
            String fileName = String.format("%s%s%s_%s_%s", UPLOAD_PATH, path, username, date, Objects.requireNonNull(file.getOriginalFilename()).replaceAll(" ", ""));

            Files.copy(file.getInputStream(), Path.of(fileName));
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }


    public Resource load(String filename) {
        try {
            final Path root = Paths.get(UPLOAD_PATH);
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public void deleteAll() {
        final Path root = Paths.get(UPLOAD_PATH);
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    public Stream<Path> loadAll() {
        try {
            final Path root = Paths.get(UPLOAD_PATH);
            return Files.walk(root, 1).filter(path -> !path.equals(root)).map(root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }

    public void delete(String oldImg) {
        try {
            File file = new File(oldImg);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}