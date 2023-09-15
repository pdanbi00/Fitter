package com.kafka.news.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KeywordProducer {
	private static final String TOPIC = "keyword";
	private final KafkaTemplate<String, String> kafkaTemplate;
	private final KafkaTemplate<String, String> kafkaDonga;
	private final KafkaTemplate<String, String> kafkaChosun;
	private final KafkaTemplate<String, String> kafkaNaver;
	private final KafkaTemplate<String, String> kafkaJoongang;

	Logger logger = LoggerFactory.getLogger(KeywordProducer.class);
	private String currentPath = System.getProperty("user.dir");
	private String path = currentPath.replace("backend-data\\news", "crawler\\news\\output\\");

	public void sendMessage(String message) {
		ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(TOPIC, message);
		future.addCallback(sCallback -> {
			logger.info("Partition : {}, Offset : {}", sCallback.getRecordMetadata().partition(),
														sCallback.getRecordMetadata().offset());
		}, fCallback -> {
			logger.error("error msg : {}", fCallback.getMessage());
		});
	}

	public void sendDonga() {
		String yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		File csv = new File(path + "DongaNews" + yesterday + ".csv");
		String line;
		try (BufferedReader br = new BufferedReader(new FileReader(csv))) {
			while ((line = br.readLine()) != null) {
				if (line.isEmpty())
					continue;
				String[] split = line.split(";");
				if (split.length < 3)
					continue;
				kafkaDonga.send(TOPIC, split[2]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendChosun() {
		String yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		File csv = new File(
			path + "WeeklyChosun" + yesterday + ".csv");
		String line;
		try (BufferedReader br = new BufferedReader(new FileReader(csv))) {
			while ((line = br.readLine()) != null) {
				if (line.isEmpty())
					continue;

				String[] split = line.split(";");
				if (split.length < 3)
					continue;
				kafkaChosun.send(TOPIC, split[2]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendJoongang() {
		String yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		File csv = new File(
			path + "JoongangSportsNews" + yesterday + ".csv");
		String line;
		try (BufferedReader br = new BufferedReader(new FileReader(csv))) {
			while ((line = br.readLine()) != null) {
				if (line.isEmpty())
					continue;
				String[] split = line.split(";");
				if (split.length < 3)
					continue;
				kafkaJoongang.send(TOPIC, split[2]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendNaver() {
		String yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		File csv = new File(
			path + "NaverSportsGeneral" + yesterday + ".csv");
		String line;
		try (BufferedReader br = new BufferedReader(new FileReader(csv))) {
			while ((line = br.readLine()) != null) {
				if (line.isEmpty())
					continue;
				String[] split = line.split(";");
				kafkaNaver.send(TOPIC, split[6]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
