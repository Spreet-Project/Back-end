package com.team1.spreet.domain.user.service;

import com.team1.spreet.domain.event.repository.EventCommentRepository;
import com.team1.spreet.domain.event.repository.EventRepository;
import com.team1.spreet.domain.feed.repository.FeedCommentRepository;
import com.team1.spreet.domain.feed.repository.FeedImageRepository;
import com.team1.spreet.domain.feed.repository.FeedLikeRepository;
import com.team1.spreet.domain.feed.repository.FeedRepository;
import com.team1.spreet.domain.shorts.repository.ShortsCommentRepository;
import com.team1.spreet.domain.shorts.repository.ShortsLikeRepository;
import com.team1.spreet.domain.shorts.repository.ShortsRepository;
import com.team1.spreet.domain.subscribe.repository.SubscribeRepository;
import com.team1.spreet.domain.user.dto.UserDto;
import com.team1.spreet.domain.user.model.Provider;
import com.team1.spreet.domain.user.model.User;
import com.team1.spreet.domain.user.model.UserRole;
import com.team1.spreet.domain.user.repository.UserRepository;
import com.team1.spreet.global.auth.jwt.JwtUtil;
import com.team1.spreet.global.error.exception.RestApiException;
import com.team1.spreet.global.error.model.ErrorStatusCode;
import com.team1.spreet.global.infra.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {
    private final SubscribeRepository subscribeRepository;
    private final EventRepository eventRepository;
    private final EventCommentRepository eventCommentRepository;
    private final ShortsRepository shortsRepository;
    private final ShortsLikeRepository shortsLikeRepository;
    private final ShortsCommentRepository shortsCommentRepository;
    private final FeedRepository feedRepository;
    private final FeedImageRepository feedImageRepository;
    private final FeedCommentRepository feedCommentRepository;

    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder bcryptPasswordEncoder;
    private final EmailService emailService;
    private final FeedLikeRepository feedLikeRepository;

    //?????? ??????
    public void signup(final UserDto.SignupRequestDto requestDto) {
        if (userRepository.findByLoginId(requestDto.getLoginId()).isPresent()) {
            throw new RestApiException(ErrorStatusCode.OVERLAPPED_ID);
        } else if (userRepository.findByNickname(requestDto.getNickname()).isPresent()) {
            throw new RestApiException(ErrorStatusCode.OVERLAPPED_NICKNAME);
        } else if (!requestDto.isEmailConfirm()) {
            throw new RestApiException(ErrorStatusCode.EMAIL_CONFIRM_EXCEPTION);
        }

        userRepository.save(requestDto.toEntity(
            bcryptPasswordEncoder.encode(requestDto.getPassword()), Provider.LOCAL));
    }

    // ?????? ??????
    @Caching(evict = {
		@CacheEvict(cacheNames = "shortsList", allEntries = true),
		@CacheEvict(cacheNames = "feedList", allEntries = true),
		@CacheEvict(cacheNames = "eventList", allEntries = true),
        @CacheEvict(cacheNames = "eventByArea", allEntries = true)
    })
    @Transactional
    public void userWithdraw(String password, User user) {
        if (bcryptPasswordEncoder.matches(password, user.getPassword())) {
            feedCommentRepository.updateDeletedTrueByUserId(user.getId());
            feedLikeRepository.deleteByUserId(user.getId());
            feedImageRepository.deleteByUserId(user.getId());
            feedRepository.updateDeletedTrueByUserId(user.getId());

            shortsCommentRepository.updateDeletedTrueByUserId(user.getId());
            shortsLikeRepository.deleteByUserId(user.getId());
            shortsRepository.updateDeletedTrueByUserId(user.getId());

            subscribeRepository.deleteByUserId(user.getId());

            eventCommentRepository.updateDeletedTrueByUserId(user.getId());
            if(user.getUserRole().equals(UserRole.ROLE_APPROVED_CREW) || user.getUserRole().equals(UserRole.ROLE_ADMIN)){
                eventRepository.updateDeletedTrueByUserId(user.getId());
            }

            user.deleteUser();
            userRepository.saveAndFlush(user);
        } else {
            throw new RestApiException(ErrorStatusCode.PASSWORD_CONFIRM_INCORRECT);
        }
    }

    // ?????????
    public UserDto.LoginResponseDto login(UserDto.LoginRequestDto requestDto, HttpServletResponse httpServletResponse) {

        // ?????? ?????? ?????? ?????? ????????? ????????? ??????
        if (userRepository.findByLoginIdAndUserRoleAndDeletedFalse(requestDto.getLoginId(), UserRole.ROLE_WAITING_CREW).isPresent()) {
            throw new RestApiException(ErrorStatusCode.WAITING_CREW_APPROVAL);
        }

        UsernamePasswordAuthenticationToken beforeAuthentication = new UsernamePasswordAuthenticationToken(requestDto.getLoginId(), requestDto.getPassword());

        Authentication afterAuthentication = authenticationManagerBuilder.getObject().authenticate(beforeAuthentication);

        String token = jwtUtil.createToken(afterAuthentication);

        httpServletResponse.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);

        return getNickname(requestDto);
    }

    // ????????? ???????????? ??????????????? ?????? ?????? ?????????
    public void resetPassword(UserDto.ResetPwRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(
                () -> new RestApiException(ErrorStatusCode.MISMATCH_EMAIL)
        );

        if (!requestDto.isEmailConfirm())
            throw new RestApiException(ErrorStatusCode.EMAIL_CONFIRM_EXCEPTION);

        if(bcryptPasswordEncoder.matches(requestDto.getPassword(), user.getPassword()))
            throw new RestApiException(ErrorStatusCode.INVALID_PASSWORD);

        user.updatePassword(bcryptPasswordEncoder.encode(requestDto.getPassword()));
        userRepository.saveAndFlush(user);
    }

    // ???????????? ??? ????????? ??????
    public void signupSendEmail(String email) throws Exception {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RestApiException(ErrorStatusCode.OVERLAPPED_EMAIL);
        }
        emailService.sendSimpleMessage(email);
    }

    // ???????????? ?????? ??? ????????? ??????
    public void resetPasswordSendEmail(String email) throws Exception {
        if (userRepository.findByEmail(email).isEmpty()) {
            throw new RestApiException(ErrorStatusCode.EMAIL_CONFIRM_NULL_EXCEPTION);
        }
        emailService.sendSimpleMessage(email);
    }

    // ????????? ?????? ??????
    public void idCheck(String loginId) {
        if (userRepository.findByLoginId(loginId).isPresent())
            throw new RestApiException(ErrorStatusCode.OVERLAPPED_ID);
    }

    // ????????? ?????? ??????
    public void nicknameCheck(String nickname) {
        if (userRepository.findByNickname(nickname).isPresent())
            throw new RestApiException(ErrorStatusCode.OVERLAPPED_NICKNAME);
    }

    // ????????? ????????????
    private UserDto.LoginResponseDto getNickname(UserDto.LoginRequestDto requestDto) {
        User user = userRepository.findByLoginId(requestDto.getLoginId()).orElseThrow(
                () -> new RestApiException(ErrorStatusCode.NOT_EXIST_USER)
        );
        return new UserDto.LoginResponseDto(user.getNickname(), user.getUserRole());
    }
}