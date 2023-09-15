package com.kafka.news.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

	@Bean
	public NewTopic keyword() {
		return TopicBuilder.name("keyword")
			.partitions(3)
			.replicas(2)
			.build();
	}

	@Bean
	public NewTopic sportTopic() {
		return TopicBuilder.name("sport_topic")
			.partitions(3)
			.replicas(2)
			.build();
	}

	@Bean
	public NewTopic healthTopic() {
		return TopicBuilder.name("health_topic")
			.partitions(3)
			.replicas(2)
			.build();
	}
}



