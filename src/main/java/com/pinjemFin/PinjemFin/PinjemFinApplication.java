package com.pinjemFin.PinjemFin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.pinjemFin.PinjemFin.models")
public class PinjemFinApplication  {

	public static void main(String[] args) {
		SpringApplication.run(PinjemFinApplication.class, args);
	}

}
