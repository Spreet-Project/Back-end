package com.team1.spreet.domain.shorts.service;

import com.team1.spreet.domain.shorts.dto.ShortsDto;
import com.team1.spreet.domain.shorts.model.Category;
import com.team1.spreet.domain.shorts.model.Shorts;
import com.team1.spreet.domain.shorts.model.ShortsLike;
import com.team1.spreet.domain.shorts.repository.ShortsCommentRepository;
import com.team1.spreet.domain.shorts.repository.ShortsLikeRepository;
import com.team1.spreet.domain.shorts.repository.ShortsRepository;
import com.team1.spreet.domain.user.model.User;
import com.team1.spreet.domain.user.model.UserRole;
import com.team1.spreet.global.error.exception.RestApiException;
import com.team1.spreet.global.error.model.ErrorStatusCode;
import com.team1.spreet.global.infra.s3.service.AwsS3Service;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

	// shorts 등록
	@CacheEvict(value = "shorts", allEntries = true)
	public void saveShorts(ShortsDto.RequestDto requestDto, User user) {
		String videoUrl = awsS3Service.uploadFile(requestDto.getFile());

		shortsRepository.saveAndFlush(requestDto.toEntity(videoUrl, user));
	}

	// shorts 수정
	@CacheEvict(value = "shorts", allEntries = true)
	public void updateShorts(ShortsDto.UpdateRequestDto requestDto, Long shortsId, User user) {
		Shorts shorts = checkShorts(shortsId);
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
	@CacheEvict(value = "shorts", allEntries = true)
	public void deleteShorts(Long shortsId, User user) {
		Shorts shorts = checkShorts(shortsId);
		if (!user.getUserRole().equals(UserRole.ROLE_ADMIN) && !user.getId().equals(shorts.getUser().getId())) {
			throw new RestApiException(ErrorStatusCode.UNAVAILABLE_MODIFICATION);
		}

		String fileName = shorts.getVideoUrl().split(".com/")[1];
		awsS3Service.deleteFile(fileName);
		deleteShortsById(shorts);
	}

	// shorts 상세조회
	@Transactional(readOnly = true)
	public ShortsDto.ResponseDto getShorts(Long shortsId, Long userId) {
		Shorts shorts = checkShorts(shortsId);

		if (userId == 0L) {   //로그인 하지 않은 user 의 경우
			return new ShortsDto.ResponseDto(shorts, false);
		} else {
			return new ShortsDto.ResponseDto(shorts, checkLike(shortsId, userId));
		}
	}

	// 카테고리별 shorts 조회(페이징)
	@Transactional(readOnly = true)
	public List<ShortsDto.ResponseDto> getShortsByCategory(Category category, int page, Long userId) {
		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
		List<Shorts> shortsByCategory = shortsRepository.findShortsByDeletedFalseAndCategory(category, pageable);

		List<ShortsDto.ResponseDto> shortsList = new ArrayList<>();

		if (userId == 0L) {   //로그인 하지 않은 user 의 경우
			for (Shorts shorts : shortsByCategory) {
				shortsList.add(new ShortsDto.ResponseDto(shorts, false));
			}
		} else {
			for (Shorts shorts : shortsByCategory) {
				shortsList.add(new ShortsDto.ResponseDto(shorts, checkLike(shorts.getId(), userId)));
			}
		}
		return shortsList;
	}

	// 메인화면에서 shorts 조회(페이징)
	@Transactional(readOnly = true)
	@Cacheable(key = "#category", value = "shorts")
	public List<ShortsDto.SimpleResponseDto> getSimpleShortsByCategory(Category category) {
		Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
		List<Shorts> shortsByCategory = shortsRepository.findShortsByDeletedFalseAndCategory(category, pageable);

		List<ShortsDto.SimpleResponseDto> shortsList = new ArrayList<>();

		for (Shorts shorts : shortsByCategory) {
			shortsList.add(new ShortsDto.SimpleResponseDto(shorts));
		}
		return shortsList;
	}

	// user 가 해당 shorts 에 좋아요를 눌렀는지 확인
	private boolean checkLike(Long shortsId, Long userId) {
		ShortsLike shortsLike = shortsLikeRepository.findByShortsIdAndUserId(shortsId, userId)
			.orElse(null);
		return shortsLike != null;
	}

	// shorts 가 존재하는지 확인
	private Shorts checkShorts(Long shortsId) {
		return shortsRepository.findByIdAndDeletedFalseWithUser(shortsId).orElseThrow(
			() -> new RestApiException(ErrorStatusCode.NOT_EXIST_SHORTS)
		);
	}

	private void deleteShortsById(Shorts shorts) {
		shortsCommentRepository.updateDeletedTrueByShortsId(shorts.getId());
		shortsLikeRepository.deleteByShortsId(shorts.getId());
		shorts.isDeleted();
	}
}
