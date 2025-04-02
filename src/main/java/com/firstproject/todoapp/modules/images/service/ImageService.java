package com.firstproject.todoapp.modules.images.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImageService {
    @Value("${storage.type}")
    private String storageType;

    @Value("${server.port}")
    private String port;

    private final Cloudinary cloudinary;


    public String uploadImage(MultipartFile file, String folder) throws IOException {
        if ("local".equals(storageType)) {
            return saveImageLocally(file, folder);
        } else {
            return uploadImageCloudinary(file, folder);
        }
    }

    private String saveImageLocally(MultipartFile file, String folder) throws IOException {
        String projectDir = System.getProperty("user.dir");
        String uploadDir = projectDir + File.separator + "uploads" + File.separator + folder + File.separator;

        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File destinationFile = new File(uploadDir + file.getOriginalFilename());
        file.transferTo(destinationFile);

        String serverUrl = "http://localhost:" + port;

        // Devolver solo la URL relativa para acceder desde el navegador
        return serverUrl + "/uploads/" + folder + "/" + file.getOriginalFilename();
    }

    private String uploadImageCloudinary(MultipartFile file, String folder) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "resource_type", "image",
                "folder", folder
        ));
        return uploadResult.get("secure_url").toString();
    }
}
