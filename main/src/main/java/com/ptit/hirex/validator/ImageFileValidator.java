package com.ptit.hirex.validator;

import com.ptit.hirex.annotation.IsValidImage;
import com.ptit.hirex.util.FileUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class ImageFileValidator implements ConstraintValidator<IsValidImage, MultipartFile> {

    private long maxSize;

    @Override
    public void initialize(IsValidImage imageFile) {
        this.maxSize = imageFile.maxSize();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        return FileUtil.validImageFile(file, maxSize, context);
    }
}
