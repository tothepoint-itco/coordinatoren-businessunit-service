package company.tothepoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class BusinessunitApplication {
	private static final String BUSINESSUNIT_EXCHANGE = "businessunit-exchange";
	private static final String BUSINESSUNIT_ROUTING = "businessunit-routing";

	public static void main(String[] args) {
		SpringApplication.run(BusinessunitApplication.class, args);
	}

	@Bean
	Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper) {
		Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
		jackson2JsonMessageConverter.setJsonObjectMapper(objectMapper);
		return jackson2JsonMessageConverter;
	}

	@Bean
	RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter jackson2JsonMessageConverter) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter);
		return rabbitTemplate;
	}

	@Bean
	String exchangeName() {
		return BUSINESSUNIT_EXCHANGE;
	}

	@Bean
	String routingKey() {
		return BUSINESSUNIT_ROUTING;
	}

	@Bean
	TopicExchange topicExchange() {
		return new TopicExchange(BUSINESSUNIT_EXCHANGE, true, false);
	}
}
