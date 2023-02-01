package com.team1.spreet.domain.shorts.service;

import com.team1.spreet.domain.shorts.dto.ShortsDto;
import com.team1.spreet.domain.shorts.model.Category;
import com.team1.spreet.domain.shorts.model.Shorts;
import com.team1.spreet.domain.shorts.repository.ShortsCommentRepository;
import com.team1.spreet.domain.shorts.repository.ShortsLikeRepository;
import com.team1.spreet.domain.shorts.repository.ShortsRepository;
import com.team1.spreet.domain.user.model.User;
import com.team1.spreet.domain.user.model.UserRole;
import com.team1.spreet.global.error.exception.RestApiException;
import com.team1.spreet.global.error.model.ErrorStatusCode;
import com.team1.spreet.global.infra.s3.service.AwsS3Service;
import com.team1.spreet.global.util.RedisUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ShortsService {

	private final ShortsRepository shortsRepository;
	private final AwsS3Service awsS3Service;
	private final ShortsLikeRepository shortsLikeRepository;
	private final ShortsCommentRepository shortsCommentRepository;
	private final RedisUtil redisUtil;

	// shorts 등록
	public void saveShorts(ShortsDto.RequestDto requestDto, User user) {
		String videoUrl = awsS3Service.uploadFile(requestDto.getFile());

		shortsRepository.saveAndFlush(requestDto.toEntity(videoUrl, user));
	}

	// shorts 수정
	@CacheEvict(key = "#requestDto.getCategory()",value = "shorts")
	public void updateShorts(ShortsDto.UpdateRequestDto requestDto, Long shortsId, User user) {
		Shorts shorts = getShortsWithUserIfExists(shortsId);
		if (!user.getId().equals(shorts.getUser().getId())) {   // 수정하려는 유저가 작성자가 아닌 경우
			throw new RestApiException(ErrorStatusCode.UNAVAILABLE_MODIFICATION);
		}

		String videoUrl;
		if (!requestDto.getFile().isEmpty()) {
			//첨부파일 수정시 기존 첨부파일 삭제
			String fileName = shorts.getVideoUrl().split(".com/")[1];
			awsS3Service.deleteFile(fileName);

			//새로운 파일 업로드
			videoUrl = awsS3Service.uploadFile(requestDto.getFile());
		} else {
			//첨부파일 수정 안함
			videoUrl = shorts.getVideoUrl();
		}
		shorts.update(requestDto.getTitle(), requestDto.getContent(), videoUrl, requestDto.getCategory());
	}


	// shorts 삭제
	public void deleteShorts(Long shortsId, User user) {
		Shorts shorts = getShortsWithUserIfExists(shortsId);
		if (!user.getUserRole().equals(UserRole.ROLE_ADMIN) && !user.getId().equals(shorts.getUser().getId())) {
			throw new RestApiException(ErrorStatusCode.UNAVAILABLE_MODIFICATION);
		}

		String fileName = shorts.getVideoUrl().split(".com/")[1];
		awsS3Service.deleteFile(fileName);
		deleteShortsById(shorts);

		redisUtil.deleteCache(shorts.getCategory());    // 해당 카테고리 캐시 삭제
	}

	// shorts 상세조회
	@Transactional(readOnly = true)
	public ShortsDto.ResponseDto getShorts(Long shortsId, Long userId) {
		if (shortsRepository.findByIdAndDeletedFalse(shortsId).isEmpty()) {
			throw new RestApiException(ErrorStatusCode.NOT_EXIST_SHORTS);
		}
		return shortsRepository.findByIdAndUserId(shortsId, userId);
	}

	// 카테고리별 shorts 조회(페이징)
	@Transactional(readOnly = true)
	public List<ShortsDto.ResponseDto> getShortsByCategory(Category category, Long page, Long userId) {
		return shortsRepository.findAllSortByNewAndCategory(category, page - 1, userId);
	}

	// 메인화면에서 shorts 조회(페이징)
	@Transactional(readOnly = true)
	@Cacheable(key = "#category", value = "shorts")
	public List<ShortsDto.MainResponseDto> getMainShortsByCategory(Category category) {
		return shortsRepository.findMainShortsByCategory(category);
	}

	// shorts 가 존재하는지 확인
	private Shorts getShortsWithUserIfExists(Long shortsId) {
		return shortsRepository.findByIdAndDeletedFalseWithUser(shortsId).orElseThrow(
			() -> new RestApiException(ErrorStatusCode.NOT_EXIST_SHORTS)
		);
	}

	// shorts 삭제시, 댓글 -> 좋아요 -> shorts 순으로 삭제
	private void deleteShortsById(Shorts shorts) {
		shortsCommentRepository.updateDeletedTrueByShortsId(shorts.getId());
		shortsLikeRepository.deleteByShortsId(shorts.getId());
		shorts.isDeleted();
	}
}
