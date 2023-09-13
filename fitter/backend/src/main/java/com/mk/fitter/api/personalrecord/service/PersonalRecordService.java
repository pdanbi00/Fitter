package com.mk.fitter.api.personalrecord.service;

import java.util.List;

import com.mk.fitter.api.personalrecord.repository.dto.PersonalRecordDto;

public interface PersonalRecordService {
	List<PersonalRecordDto> getRecordList(Integer integer);

	PersonalRecordDto getRecord(int personalRecordId) throws Exception;
}
