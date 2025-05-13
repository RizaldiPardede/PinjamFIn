package com.pinjemFin.PinjemFin.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@Service
public class CloudinaryService {
    @Autowired
    private Cloudinary cloudinary;

    public String uploadImage(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return uploadResult.get("secure_url").toString(); // URL gambar
    }

    public void deleteImageByUrl(String imageUrl) throws IOException {
        String publicId = extractPublicIdFromUrl(imageUrl);
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }

    private String extractPublicIdFromUrl(String imageUrl) {
        String[] parts = imageUrl.split("/");
        int index = Arrays.asList(parts).indexOf("upload");
        if (index != -1 && index + 1 < parts.length) {
            StringBuilder publicId = new StringBuilder();
            for (int i = index + 2; i < parts.length; i++) {
                publicId.append(parts[i]);
                if (i < parts.length - 1) publicId.append("/");
            }
            return publicId.toString().replaceFirst("\\.[^.]+$", ""); // hapus ekstensi
        }
        throw new IllegalArgumentException("URL Cloudinary tidak valid: " + imageUrl);
    }
}
