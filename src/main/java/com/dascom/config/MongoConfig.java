package com.dascom.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.util.StringUtils;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;

@Configuration
public class MongoConfig {

	@Value(value = "${mongo.host}")
	private String host;

	@Value(value = "${mongo.port}")
	private Integer port;

	@Value(value = "${mongo.databaseName}")
	private String database;

	@Value(value = "${mongo.username}")
	private String username;

	@Value(value = "${mongo.password}")
	private String password;


	@Primary
	@Bean
	@Value("mongoTemplate")
	public MongoTemplate mongoTemplate() throws Exception {
		MongoDbFactory authFactory = mongoFactory(host, port, username, password, database);
		MappingMongoConverter mappingMongoConverter = mappingMongoConverter(authFactory);
		MongoTemplate mongoTemplate = new MongoTemplate(authFactory, mappingMongoConverter);
		ReadPreference preference = ReadPreference.secondary();
		mongoTemplate.setReadPreference(preference);
		return mongoTemplate;
	}

	@Primary
	@Bean
	public MongoDbFactory mongoFactory(String host, Integer port, String username, String password, String database)
			throws Exception {
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			return new SimpleMongoDbFactory(new MongoClient(host, port), database);
		}
		MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
		builder.threadsAllowedToBlockForConnectionMultiplier(10);
		MongoClientOptions mongoClientOptions = builder.build();
		ServerAddress serverAddress = new ServerAddress(host, port);
		List<MongoCredential> credentialsList = new ArrayList<MongoCredential>();

		credentialsList.add(MongoCredential.createScramSha1Credential(username, database, password.toCharArray()));
		return new SimpleMongoDbFactory(new MongoClient(serverAddress, credentialsList, mongoClientOptions), database);

	}

	@Primary
	@Bean
	public MappingMongoConverter mappingMongoConverter(MongoDbFactory factory) {
		DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
		MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, new MongoMappingContext());
		mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
		return mappingConverter;
	}

}
