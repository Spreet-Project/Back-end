package com.team1.spreet.global.util;

import com.team1.spreet.domain.shorts.model.Category;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
@Slf4j
public class RedisUtil {

	@CacheEvict(key = "#category", value = "shorts")
	public void deleteCache(Category category) {
		log.info("deleted Cache category {}", category);
	}
}
