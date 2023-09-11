package com.mk.fitter.api.namedwod.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mk.fitter.api.namedwod.repository.WodRecordRepository;
import com.mk.fitter.api.namedwod.repository.WodRepository;
import com.mk.fitter.api.namedwod.repository.entity.WodRecordDto;
import com.mk.fitter.api.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WodServiceImpl implements WodService {

	private final WodRecordRepository wodRecordRepository;
	private final UserRepository userRepository;
	private final WodRepository wodRepository;

	public boolean createWodRecord(WodRecordDto wodRecordDto) throws Exception {
		WodRecordDto byWodIdAndUserId = wodRecordRepository.findByWod_IdAndUser_Id(wodRecordDto.getWod().getId(),
			wodRecordDto.getUser().getId());
		if (byWodIdAndUserId != null) {
			throw new Exception("이미 존재하는 기록입니다.");
		} else {
			wodRecordRepository.save(wodRecordDto);
		}
		return true;
	}

	@Override
	public List<WodRecordDto> getWodRecordList() {
		return wodRecordRepository.findAll();
	}
}
