package com.mk.fitter.api.personalrecord.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mk.fitter.api.personalrecord.repository.PersonalRecordRepository;
import com.mk.fitter.api.personalrecord.repository.dto.PersonalRecordDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PersonalRecordServiceImpl implements PersonalRecordService {

	private final PersonalRecordRepository personalRecordRepository;

	@Override
	public List<PersonalRecordDto> getRecordList(Integer userId) {

		return personalRecordRepository.findByUserDto_Id(userId);
	}

	@Override
	public PersonalRecordDto getRecord(int personalRecordId) throws Exception {
		Optional<PersonalRecordDto> byId = personalRecordRepository.findById(personalRecordId);
		if (byId.isEmpty()) {
			throw new Exception("기록이 존재하지 않습니다.");
		}
		return byId.get();
	}
}
