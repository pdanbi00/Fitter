package com.mk.fitter.api.namedwod.service;

import java.util.List;

import com.mk.fitter.api.namedwod.repository.entity.WodRecordDto;

public interface WodService {
	boolean createWodRecord(WodRecordDto wodRecordDto) throws Exception;

	List<WodRecordDto> getWodRecordList();
}
