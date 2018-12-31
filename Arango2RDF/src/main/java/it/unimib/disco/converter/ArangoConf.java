package it.unimib.disco.converter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.arangodb.ArangoDB;
import com.arangodb.ArangoDB.Builder;
import com.arangodb.springframework.annotation.EnableArangoRepositories;
import com.arangodb.springframework.config.ArangoConfiguration;;

@Configuration
@EnableArangoRepositories(basePackages = { "it.unimib.disco.converter" })
public class ArangoConf implements ArangoConfiguration {

	@Value("${arango.host}")
	private String host;
	@Value("${arango.port}")
	private int port;
	@Value("${arango.user:}")
	private String user;
	@Value("${arango.password}")
	private String password;
	@Value("${arango.database}")
	private String database;

	@Override
	public Builder arango() {
		return new ArangoDB.Builder().host(host, port).user(user).password(password);
	}

	@Override
	public String database() {
		return database;
	}
}
