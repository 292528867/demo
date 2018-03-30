package com.yk.example;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.yk.example.config.JwtFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.io.IOException;

@SpringBootApplication
public class DemoApplication  {

	@Value("${jackson.indent.output}")
	private boolean jacksonIndentOutput = false;

	@Bean
	public FilterRegistrationBean jwtFilter() {
		final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(new JwtFilter());
		registrationBean.addUrlPatterns("/secure/*");

		return registrationBean;
	}

	@Bean
	@Primary
	public ObjectMapper objectMapper() {

		ObjectMapper objectMapper = new ObjectMapper();
		if (jacksonIndentOutput) {
			objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		}

		objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
			@Override
			public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
				jgen.writeString("");
			}
		});

		Hibernate4Module hibernateMoudle = new Hibernate4Module();
		hibernateMoudle.disable(Hibernate4Module.Feature.USE_TRANSIENT_ANNOTATION);

		return objectMapper
				.registerModule(new JodaModule())
				.registerModule(hibernateMoudle);

	}


	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
