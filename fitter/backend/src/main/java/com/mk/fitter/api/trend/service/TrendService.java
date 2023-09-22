package com.mk.fitter.api.trend.service;

import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.mk.fitter.api.trend.dto.HealthWordDto;
import com.mk.fitter.api.trend.dto.SportsWordDto;
import com.mk.fitter.api.trend.repository.HealthWordRepository;
import com.mk.fitter.api.trend.repository.SportsWordRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TrendService {

    private final SportsWordRepository sportsWordRepository;
    private final HealthWordRepository healthWordRepository;

    @Value("${naver.api.url}")
    private String naverApiUrl;

    @Value("${naver.api.search.url}")
    private String naverSearchUrl;

    @Value("${naver.api.client.id}")
    private String naverClientId;

    @Value("${naver.api.client.secret}")
    private String naverClientSecret;


    @Transactional(readOnly = true)
    public List<SportsWordDto> getAllSports() {
        return  sportsWordRepository.findAllByOrderByCountDesc().stream()
                .map(SportsWordDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<HealthWordDto> getTop5Healths() {
        return healthWordRepository.findTop5ByOrderByCountDesc().stream()
            .map(HealthWordDto::new)
            .collect(Collectors.toList());
    }

    public String getHealthNews(String keyword) {

        ByteBuffer buffer = StandardCharsets.UTF_8.encode(keyword);
        String encode = StandardCharsets.UTF_8.decode(buffer).toString();

        URI uri = UriComponentsBuilder
            .fromUriString(naverApiUrl)
            .path(naverSearchUrl)
            .queryParam("query", encode)
            .queryParam("display", 5)
            .queryParam("start", 1)
            .queryParam("sort", "sim")
            .encode()
            .build()
            .toUri();

        RestTemplate restTemplate = new RestTemplate();

        RequestEntity<Void> req = RequestEntity
            .get(uri)
            .header("X-Naver-Client-Id", naverClientId)
            .header("X-Naver-Client-Secret", naverClientSecret)
            .build();

        ResponseEntity<String> result = restTemplate.exchange(req, String.class);
        return result.getBody();

    }

}
