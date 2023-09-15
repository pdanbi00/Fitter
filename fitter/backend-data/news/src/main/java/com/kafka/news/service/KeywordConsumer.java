package com.kafka.news.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KeywordConsumer {

	private static final String TOPIC = "keyword";
	private static final String OUTPUT_TOPIC = "sport-topic";

	private final KafkaTemplate<String, String> kafkaTemplate;

	private final Set<String> sportSet = new HashSet<>(Arrays.asList("골프", "근대5종", "농구", "다이빙", "럭비", "레슬링", "배구", "배드민턴", "복싱", "브레이킹", "비치발리볼", "사격",
		"사이클", "서핑", "수구", "수영", "승마", "스케이트보드", "스포츠클라이밍", "아티스틱스위밍", "역도", "양궁", "요트", "유도", "육상", "조정", "체조",
		"축구", "카누", "탁구", "태권도", "테니스", "트라이애슬론", "펜싱", "하키", "핸드볼", "가라테", "라크로스", "라켓", "로크", "모터보트", "소프트볼",
		"야구", "주드폼", "줄다리기", "크로케", "크리켓", "폴로", "펠로타", "글라이딩", "글리마", "라칸", "롤러하키", "무도", "미식축구", "볼링", "사바트",
		"오지풋볼", "수상스키", "코프볼", "페세팔로", "노르딕복합", "루지", "바이애슬론", "봅슬레이", "쇼트트랙", "스노보드", "스켈레톤", "스키점프", "스피드스케이팅",
		"아이스하키", "알파인스키", "컬링", "크로스컨트리", "프리스타일스키", "피겨스케이팅", "밀리터리패트롤", "개썰매", "밴디", "스키저링", "스피드스키", "아이스스톡",
		"노르딕스키", "필드하키", "택견", "쿵푸", "씨름", "킥복싱", "팔씨름", "주짓수", "링", "철봉", "도마", "평균대", "마루운동", "싱크로나이즈", "라켓볼",
		"리얼테니스", "스쿼시", "정구", "킨볼", "피구", "게이트볼", "족구", "티볼", "세팍타크로", "보치아", "당구", "마라톤", "멀리뛰기", "높이뛰기", "장대높이뛰기",
		"창던지기", "원반던지기", "포환던지기", "장애물달리기", "크로스핏", "UFC", "MMA", "요가", "헬스", "발레"));

	Logger logger = LoggerFactory.getLogger(KeywordConsumer.class);

	@KafkaListener(topics = TOPIC, groupId = "keywordCount", concurrency = "3")
	public void receiveMessage(String message) throws IOException {
		Komoran komoran = new Komoran(DEFAULT_MODEL.LIGHT);
		message = message.strip();
		KomoranResult analyze = komoran.analyze(message);
		List<String> nouns = analyze.getNouns();
		for (String noun : nouns) {
			if (isSportsKeyword(noun)) {
				kafkaTemplate.send(OUTPUT_TOPIC, noun);
				logger.info("sport word : {}", noun);
			}
		}
	}

	private boolean isSportsKeyword(String message) {
		return sportSet.contains(message);
	}
}

