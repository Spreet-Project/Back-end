package com.team1.spreet.domain.admin.service;

import com.team1.spreet.domain.admin.domain.BadWord;
import com.team1.spreet.domain.admin.dto.BadWordDto;
import com.team1.spreet.domain.admin.repository.BadWordRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BadWordService {

	private final BadWordRepository badWordRepository;
	private final RedisUtil redisUtil;


	// 비속어 레디스 저장
	public void addBadWord() {
		List<BadWord> badWordList = badWordRepository.findAll();
		for (BadWord badWord : badWordList) {
			redisUtil.addBadWord(badWord.getContent());
		}
	}

	// 비속어 DB 저장
	public void createBadWord(BadWordDto.RequestDto requestDto) {
		badWordRepository.save(new BadWord(requestDto.getContent()));
	}

	// 비속어 체크
	public String checkBadWord(String content) {
		List<BadWord> badWordList = badWordRepository.findAll();
		for (BadWord badWord : badWordList) {
			if (content.contains(badWord.getContent())) {
				int len = badWord.getContent().length();
				String sub = "*".repeat(len);
				content = content.replace(badWord.getContent(), sub);
			}
		}
		return content;
	}

}
