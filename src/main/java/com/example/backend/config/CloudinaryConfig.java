package com.example.backend.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Configuration
public class CloudinaryConfig {

    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Value("${cloudinary.api-key}")
    private String apiKey;

    @Value("${cloudinary.api-secret}")
    private String apiSecret;


    @Bean
    public Cloudinary cloudinary() {
        // validasi sederhana agar tidak mengirim "placeholder" ke Cloudinary
        if (!StringUtils.hasText(cloudName) || !StringUtils.hasText(apiKey) || !StringUtils.hasText(apiSecret) ||
                "${CLOUDINARY_CLOUD_NAME}".equals(cloudName) ||
                "${CLOUDINARY_API_KEY}".equals(apiKey) ||
                "${CLOUDINARY_API_SECRET}".equals(apiSecret)) {
            throw new IllegalStateException(
                    "Cloudinary configuration is missing. Pastikan environment CLOUDINARY_CLOUD_NAME, CLOUDINARY_API_KEY, CLOUDINARY_API_SECRET terisi.");
        }

        // Jangan log apiKey/apiSecret penuh ke console. Gunakan logger di level DEBUG.
        org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CloudinaryConfig.class);
        if (log.isDebugEnabled()) {
            log.debug("[CloudinaryConfig] cloudName={}", cloudName);
            log.debug("[CloudinaryConfig] apiKey.length={} apiSecret.length={}",
                    apiKey == null ? 0 : apiKey.length(), apiSecret == null ? 0 : apiSecret.length());
        }

        Map<String, String> config = ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret);

        return new Cloudinary(config);
    }
}

