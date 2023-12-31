package com.mk.fitter.api.namedwod.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mk.fitter.api.namedwod.repository.dto.WodRecordDto;

@Repository
public interface WodRecordRepository extends JpaRepository<WodRecordDto, Integer> {
	List<WodRecordDto> findAll();

	boolean deleteById(int wodRecordId);

	List<WodRecordDto> findByWod_Id(int wodId);

	List<WodRecordDto> findByWod_IdAndUser_IdOrderByCreateDateDesc(int wodId, int userId);

	List<WodRecordDto> findByUser_Id(int userId);

	@Query(value =
		"SELECT w.id, w.wod_id, w.user_id, w.time, w.count, w.create_date, RANK() OVER(ORDER BY time ASC) AS ranking FROM\n"
			+ "( SELECT user_id, wod_id, min(time) AS min_time FROM wod_record WHERE wod_id = :wodId GROUP BY user_id, wod_id)\n"
			+ "AS r INNER JOIN wod_record AS w ON r.user_id = w.user_id AND r.min_time = w.time AND r.wod_id = w.wod_id", nativeQuery = true)
	Page<WodRecordDto> findRankById(@Param("wodId") int wodId, Pageable pageable);

	@Query(value = "select w.id, w.wod_id, w.user_id, w.time, w.count, w.create_date, r.ranking \n"
		+ "FROM (SELECT user_id, wod_id, min(time) AS min_time, RANK() OVER(ORDER BY min(time) ASC) as ranking FROM wod_record WHERE wod_id = :wodId group by user_id, wod_id)\n"
		+ "AS r INNER JOIN wod_record AS w ON r.user_id = w.user_id AND r.min_time = w.time AND r.wod_id = w.wod_id\n"
		+ "where w.user_id = :userId", nativeQuery = true)
	Map<String, Object> findRankByIdAndUserId(@Param("wodId") int wodId, @Param("userId") int userId);
}
