package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {
	WeeklyLog weeklyLog = new WeeklyLog();
	DailyLog dailyLog = new DailyLog();

	@Test
	void testCalories() {
		if (weeklyLog.getCalories() == dailyLog.getCalories()) {
			System.out.println("Works as intended");
		} else {
			System.out.println("Doesn't work as intended");
		}
	}

}
