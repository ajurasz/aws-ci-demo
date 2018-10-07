package com.example.demo;

import com.amazonaws.util.EC2MetadataUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Entity
@Table(name = "beers")
class Beer {

	@Id @GeneratedValue
	private Long id;

	private String name;
}

interface BeerRepository extends JpaRepository<Beer, Long> { }

@Controller
class DemoController {

	private final BeerRepository beerRepository;

	DemoController(BeerRepository beerRepository) {
		this.beerRepository = beerRepository;
	}

	@GetMapping("/beers")
	ResponseEntity<List<Beer>> beers() {
		return ok(beerRepository.findAll());
	}

	@GetMapping("/ip")
	ResponseEntity<String> ip() {
		return ok(EC2MetadataUtils.getPrivateIpAddress());
	}

	@GetMapping("/crash")
	ResponseEntity<?> crash() {
		System.out.println("About to terminate ...");
		OOMGenerator.generateOOM();
		return ok().build();
	}

	@GetMapping("/terminate")
	ResponseEntity<?> terminate() {
		System.out.println("About to terminate ...");
		System.exit(1);
		return ok().build();
	}

	@GetMapping("/health")
	ResponseEntity<?> health() {
		return ok().build();
	}

	@GetMapping("/swap")
	ResponseEntity<String> swap(@RequestParam String s) {
	    return ok(StringUtils.swapCase(s));
    }
}

