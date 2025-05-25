package jsl.group.order_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
@ConfigurationProperties(prefix = "app")
public class OrderConfigurationProperties {
    private String version;
    private String catalogService;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCatalogService() {
        return catalogService;
    }

    public void setCatalogService(String catalogService) {
        this.catalogService = catalogService;
    }
}
