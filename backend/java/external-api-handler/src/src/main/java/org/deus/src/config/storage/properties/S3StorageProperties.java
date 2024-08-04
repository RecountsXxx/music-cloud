package org.deus.src.config.storage.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "storage.s3")
@Component
@Data
@EqualsAndHashCode(callSuper = true)
public class S3StorageProperties extends BaseStorageProperties {
    private String region;
}
