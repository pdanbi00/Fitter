package com.mk.fitter.api.namedwod.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mk.fitter.api.namedwod.repository.entity.WodRecordDto;
import com.mk.fitter.api.namedwod.service.WodService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/named-wod")
@RequiredArgsConstructor
@Slf4j
public class WodController {

	private final WodService wodService;

	@GetMapping("list")
	public ResponseEntity<?> getWodRecordList() {
		try {
			List<WodRecordDto> wodRecordList = wodService.getWodRecordList();
			return new ResponseEntity<>(wodRecordList, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/create")
	public ResponseEntity<?> createWodRecord(@RequestHeader String accessToken,
		@RequestBody WodRecordDto wodRecordDto) {
		// 토큰값으로 유저 가져오거나, 프론트에서 유저 id 받아야 함
		boolean result = false;
		try {
			result = wodService.createWodRecord(wodRecordDto);
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
