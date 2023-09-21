package com.kafka.news.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "health_word", indexes = {@Index(name = "idx_name", columnList = "name")})
public class HealthWord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "health_word_id")
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    private int count;

    @Builder
    public HealthWord(String name, int count){
        this.name = name;
        this.count = count;
    }

    // 건강 키워드 증가
    public void updateCount(int newCount){
        this.count += newCount;
    }
}
