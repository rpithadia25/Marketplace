package com.pithadia.marketplace;

import com.pithadia.marketplace.service.DataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MarketplaceApplication {

	@Autowired
    DataLoader dataLoader;

	public static void main(String[] args) {
		SpringApplication.run(MarketplaceApplication.class, args);
	}
}
