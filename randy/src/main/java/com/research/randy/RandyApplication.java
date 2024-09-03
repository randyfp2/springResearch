package com.research.randy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import org.springframework.cache.CacheManager;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
//@EnableCaching
public class RandyApplication {

	public static void main(String[] args) {
		SpringApplication.run(RandyApplication.class, args);
	}
	/*
	@Bean
	public CacheManager cacheManager() {
		return new ConcurrentMapCacheManager("configCache");
	}
	 */
}
