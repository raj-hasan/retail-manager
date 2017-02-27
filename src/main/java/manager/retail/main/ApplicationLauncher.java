package manager.retail.main;

import manager.retail.service.shop.ShopLocator;
import manager.retail.service.shop.ShopLocatorImpl;
import manager.retail.datastore.ShopInMemoryArray;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @CreatedBy Hasan on 26-Feb-2017
 */
@SpringBootApplication(scanBasePackages = "manager.retail")
public class ApplicationLauncher {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationLauncher.class, args);
        System.out.println(RetailMessages.SUCCESS);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public ShopInMemoryArray shopListHolder() {
        return new ShopInMemoryArray();
    }

    @Bean
    public ShopLocator shopLocatorService() {
        return new ShopLocatorImpl();
    }
}
