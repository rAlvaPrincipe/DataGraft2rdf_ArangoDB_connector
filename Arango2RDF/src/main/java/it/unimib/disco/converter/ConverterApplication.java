package it.unimib.disco.converter;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import it.unimib.disco.converter.runner.CampaignsRunner;
import it.unimib.disco.converter.runner.EdgesRunner;


@SpringBootApplication
public class ConverterApplication {

	public static void main(final String... args) {
		Class<CommandLineRunner>[] runners = new Class[] { CampaignsRunner.class, EdgesRunner.class };
		System.exit(SpringApplication.exit(SpringApplication.run(runners, args)));
	}

}
