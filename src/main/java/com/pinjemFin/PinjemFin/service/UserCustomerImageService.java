package com.pinjemFin.PinjemFin.service;

import com.pinjemFin.PinjemFin.models.UserCustomerImage;
import com.pinjemFin.PinjemFin.models.UsersCustomer;
import com.pinjemFin.PinjemFin.repository.UsersCustomerImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserCustomerImageService {

    @Autowired
    private UsersCustomerImageRepository usersCustomerImageRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private CustomerService customerService;

    public String uploadImage(String token, String imageType, MultipartFile file) throws Exception {
        // Ambil user dari token
        UsersCustomer user = customerService.getUserCustomer(
                customerService.getUserCustomerIdFromToken(token)
        );

        // Cari apakah user ini sudah pernah upload imageType ini
        Optional<UserCustomerImage> existingImageOpt =
                usersCustomerImageRepository.findByUserCustomerAndImageType(user, imageType);

        // Upload gambar baru ke Cloudinary
        String newImageUrl = cloudinaryService.uploadImage(file);

        if (existingImageOpt.isPresent()) {
            // Hapus gambar lama dari Cloudinary
            UserCustomerImage existingImage = existingImageOpt.get();
            cloudinaryService.deleteImageByUrl(existingImage.getImageUrl());

            // Update data di database
            existingImage.setImageUrl(newImageUrl);
            existingImage.setCreatedAt(LocalDateTime.now());
            usersCustomerImageRepository.save(existingImage);

            return "Gambar " + imageType + " berhasil diperbarui.";
        } else {
            // Buat data baru
            UserCustomerImage newImage = new UserCustomerImage();
            newImage.setUserCustomer(user);
            newImage.setImageType(imageType);
            newImage.setImageUrl(newImageUrl);
            newImage.setCreatedAt(LocalDateTime.now());
            usersCustomerImageRepository.save(newImage);

            return "Gambar " + imageType + " berhasil diunggah.";
        }
    }
    public String getImageProfile(String token) throws Exception {
        UsersCustomer user = customerService.getUserCustomer(customerService.getUserCustomerIdFromToken(token));
        UserCustomerImage userCustomerImage = usersCustomerImageRepository.findProfileImageByCustomerId(user.getId_user_customer()).get();
        return userCustomerImage.getImageUrl();
    }

}
