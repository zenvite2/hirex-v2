package com.ptit.hirex.util;

import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class FileUtil {

    public static boolean isValidBusinessUpload(MultipartFile businessFile, long maxSize, ConstraintValidatorContext context) {
        String contentType = businessFile.getContentType();

        if (!contentType.equals("image/png") && !contentType.equals("image/jpeg") && !contentType.equals("application/pdf")) {
            Util.setCustomValidateMessage(context, "{validation.invalid.business}");
            return false;
        }

        return businessFile.getSize() <= maxSize;
    }

    public static boolean validImageFile(MultipartFile file, long maxSize, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return true;
        }

        String contentType = file.getContentType();
        if (!"image/png".equals(contentType) && !"image/jpeg".equals(contentType)) {
            Util.setCustomValidateMessage(context, "{validation.invalid.image}");
            return false;
        }

        return file.getSize() <= maxSize;
    }

    public static boolean validAchievementAttached(MultipartFile imageFile, long maxSize, ConstraintValidatorContext context) {
        if (imageFile == null || imageFile.isEmpty()) {
            return true;
        }

        String contentType = imageFile.getContentType();

        if (!contentType.equals("image/png") && !contentType.equals("image/jpeg")) {
            Util.setCustomValidateMessage(context, "{validation.invalid.achievement}");
            return false;
        }

        return imageFile.getSize() <= maxSize;
    }
}
