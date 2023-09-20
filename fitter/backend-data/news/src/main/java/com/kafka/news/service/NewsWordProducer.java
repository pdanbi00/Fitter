package com.kafka.news.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kafka.news.repository.HealthWordRepository;
import com.kafka.news.repository.SportsWordRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NewsWordProducer {

	private final KafkaTemplate<String, String> kafkaSports;
	private final KafkaTemplate<String, String> kafkaHealth;

	private final HealthWordRepository healthWordRepository;
	private final SportsWordRepository sportsWordRepository;
	private final String currentPath = System.getProperty("user.dir");

	Logger logger = LoggerFactory.getLogger(NewsWordProducer.class);

	@Scheduled(cron = "0 0 12 * * ?") // 정오 자동 실행
	@Transactional
	public void sendNewsKeyword(){
		healthWordRepository.deleteAll();
		sportsWordRepository.resetCountToZero();
		sendSportsWord(kafkaSports, "sports", "sports-topic");
		sendHealthWord(kafkaHealth, "health", "health-topic");
	}

	private void sendSportsWord(KafkaTemplate<String, String> kafkaTemplate, String type, String topic){
		ExecutorService executorService = Executors.newFixedThreadPool(4); // 병렬로 처리할 스레드 풀 생성
		CompletableFuture<Void> chosun = CompletableFuture.runAsync(() -> sendNewsData(kafkaTemplate,"NaverSportsNews", type, topic), executorService);
		CompletableFuture<Void> donga = CompletableFuture.runAsync(() -> sendNewsData(kafkaTemplate,"JoongangSportsNews", type, topic), executorService);
		CompletableFuture<Void> joongang = CompletableFuture.runAsync(() -> sendNewsData(kafkaTemplate,"DongaSportsNews", type, topic), executorService);
		CompletableFuture<Void> naver = CompletableFuture.runAsync(() -> sendNewsData(kafkaTemplate,"ChosunSportsNews", type, topic), executorService);

		// 모든 작업이 완료될 때까지 대기
		CompletableFuture<Void> allOf = CompletableFuture.allOf(chosun, donga, joongang, naver);

		try {
			allOf.get(); // 모든 작업이 완료될 때까지 대기
			executorService.shutdown(); // 스레드 풀 종료
			if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) { // 최대 5초까지 스레드 풀 종료 기다림
				executorService.shutdownNow(); // 시간 초과 시 강제 종료
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt(); // 인터럽트 상태 복원
		} finally {
			if (!executorService.isTerminated()) {
				executorService.shutdownNow(); // 아직 완전히 종료되지 않았으면 강제 종료
			}
		}
	}

	private void sendHealthWord(KafkaTemplate<String, String> kafkaTemplate, String type, String topic){
		ExecutorService executorService = Executors.newFixedThreadPool(4); // 병렬로 처리할 스레드 풀 생성
		CompletableFuture<Void> chosun = CompletableFuture.runAsync(() -> sendNewsData(kafkaTemplate, "ChosunHealthNews", type, topic), executorService);
		CompletableFuture<Void> donga = CompletableFuture.runAsync(() -> sendNewsData(kafkaTemplate, "DongaHealthNews", type, topic), executorService);
		CompletableFuture<Void> joongang = CompletableFuture.runAsync(() -> sendNewsData(kafkaTemplate, "JoongangHealthNews", type, topic), executorService);
		CompletableFuture<Void> naver = CompletableFuture.runAsync(() -> sendNewsData(kafkaTemplate, "NaverHealthNews", type, topic), executorService);

		// 모든 작업이 완료될 때까지 대기
		CompletableFuture<Void> allOf = CompletableFuture.allOf(chosun, donga, joongang, naver);

		try {
			allOf.get(); // 모든 작업이 완료될 때까지 대기
			executorService.shutdown(); // 스레드 풀 종료
			if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) { // 최대 5초까지 스레드 풀 종료 기다림
				executorService.shutdownNow(); // 시간 초과 시 강제 종료
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt(); // 인터럽트 상태 복원
		} finally {
			if (!executorService.isTerminated()) {
				executorService.shutdownNow(); // 아직 완전히 종료되지 않았으면 강제 종료
			}
		}
	}

	private void sendNewsData(KafkaTemplate<String, String> kafkaTemplate, String newsName, String type, String topic) {
		String yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String path = currentPath.replace("backend-data\\news", "crawler\\news\\output\\" + type + "\\");
		File csv = new File(path + newsName + yesterday + ".csv");

		if (!csv.exists()){
			logger.info("뉴스 파일이 존재하지 않습니다. {}", csv.getPath());
			return;
		}

		try (BufferedReader br = new BufferedReader(new FileReader(csv))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.isEmpty())
					continue;
				String[] split = line.split(";");
				for (String s : split) {
					kafkaTemplate.send(topic, s);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
