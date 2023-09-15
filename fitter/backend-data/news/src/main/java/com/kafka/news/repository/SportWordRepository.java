package com.kafka.news.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kafka.news.entity.SportWord;

@Repository
public interface SportWordRepository extends JpaRepository<SportWord, Integer> {

	Optional<SportWord> findByName(String name);
}
