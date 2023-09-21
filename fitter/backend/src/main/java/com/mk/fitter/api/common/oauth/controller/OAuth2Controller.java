package com.mk.fitter.api.common.oauth.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mk.fitter.api.common.oauth.Role;
import com.mk.fitter.api.common.oauth.service.CustomOAuth2UserService;
import com.mk.fitter.api.common.oauth.service.OAuth2Service;
import com.mk.fitter.api.common.service.JwtService;
import com.mk.fitter.api.user.repository.dto.UserDto;
import com.mk.fitter.api.user.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/oauth2")
@RequiredArgsConstructor
@Slf4j
@Api(tags = {"카카오 로그인 api"})
public class OAuth2Controller {

	private final OAuth2Service oAuth2Service;
	private final UserService userService;
	private final JwtService jwtService;
	private final String BEARER = "Bearer ";

	@GetMapping("/kakao")
	@ApiOperation(value = "카카오 로그인 api", notes = "카카오 로그인 api")
	public ResponseEntity<String> kakaoCallback(
		HttpServletRequest request, HttpServletResponse response,
		@ApiParam(value = "카카오 token", required = true)
		@RequestHeader(name = "Authorization") String kakaoToken) {

		try {
			log.info("로그인 api 진입");
			UserDto userDto = oAuth2Service.kakaoLogin(kakaoToken);

			// access, refresh 토큰 생성
			String accessToken = BEARER + jwtService.createAccessToken(userDto.getId(), userDto.getEmail());
			String refreshToken = BEARER + jwtService.createRefreshToken();

			// refresh token db에 update
			jwtService.updateRefreshToken(userDto.getEmail(), refreshToken);

			// 헤더에 access, refresh 추가
			jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);

			// 헤더에 nickname, id 보냄
			response.addHeader("id", String.valueOf(userDto.getId()));
			response.addHeader("nickname", userDto.getNickname());

			return new ResponseEntity<>("login success!", HttpStatus.OK);
		} catch (Exception e) {
			log.info("소셜 로그인에 실패했습니다. 에러 메시지 : {}", e.getMessage());
			return new ResponseEntity<>("login failed", HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "회원가입 시 회원정보 저장", notes = "회원정보 저장")
	@PostMapping("/user-info")
	public ResponseEntity<UserDto> saveUserInfo(@ApiParam(value = "회원정보") @RequestBody UserDto userDto) {
		try {
			UserDto user = userService.saveUserInfo(userDto);
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (Exception e) {
			log.error("saveUserInfo :: {}", e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
