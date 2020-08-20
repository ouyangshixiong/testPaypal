package com.mmgss.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest
class TestPaypalApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	public void testDataTimeFormatter(){
		LocalDateTime dt = LocalDateTime.now();
		System.out.println(dt);
//		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYYMMDDHHmmss");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		System.out.println(dtf.format(dt));
	}

}
