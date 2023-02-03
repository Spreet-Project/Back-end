package com.team1.spreet.global.util;

import com.team1.spreet.domain.user.model.User;
import com.team1.spreet.global.auth.security.UserDetailsImpl;
import com.team1.spreet.global.error.exception.RestApiException;
import com.team1.spreet.global.error.model.ErrorStatusCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@NoArgsConstructor
public class SecurityUtil {

	public static User getCurrentUser() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null) {
			throw new RestApiException(ErrorStatusCode.NOT_EXIST_AUTHORIZATION);
		}

		if (authentication.getPrincipal() instanceof UserDetails) {
			UserDetailsImpl springSecurityUser = (UserDetailsImpl) authentication.getPrincipal();
			return springSecurityUser.getUser();
		}else {
			return null;
		}
	}
}
