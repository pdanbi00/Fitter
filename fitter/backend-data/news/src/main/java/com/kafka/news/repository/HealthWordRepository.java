package com.kafka.news.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kafka.news.entity.HealthWord;

@Repository
public interface HealthWordRepository extends JpaRepository<HealthWord, Integer> {

}
