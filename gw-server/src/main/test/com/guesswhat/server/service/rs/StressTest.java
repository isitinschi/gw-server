package com.guesswhat.server.service.rs;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.guesswhat.server.service.rs.dto.ImageType;
import com.guesswhat.server.service.rs.dto.QuestionDTO;

// 6108 seconds
@Ignore
public class StressTest extends AbstractServiceTest {
	
	private static final int QUESTIONS_COUNT = 500;
	private static final int USERS_COUNT = 1000;
	private static final int RECORDS_COUNT = 10;
	private static final int ITERATIONS = 10;
	
	@Test
	public void testCreateQuestions() {
		createQuestions();
	}
	
	@Test
	public void testCreateRecords() {		
		for (int g = 0; g < ITERATIONS; ++g) {
			for (int i = 0; i < USERS_COUNT; ++i) {
				for (int j = 0; j < RECORDS_COUNT; ++j) {
					createRecord("user" + i, j + i);				
				}
			}
		}
	}
	
	@Test
	public void testSimulateUserActivity() {
		for (int g = 0; g < ITERATIONS; ++g) {
			for (int i = 0; i < USERS_COUNT; ++i) {
				List<QuestionDTO> questions = findQuestions();
				for (int q = 0; q < questions.size() && q < 10; ++q) {
					String questionId = questions.get(q).getId();
					for (ImageType type : ImageType.values()) {
						downloadImage(questionId, type, "question");
						downloadImage(questionId, type, "answer");
					}
				}
				createRecord("user" + i, i + g);
				findQuestions();
			}
		}
	}
	
	private void createQuestions() {
		for (int i = 0; i < QUESTIONS_COUNT; ++i) {
			String answer1 = "answer1-" + i;
			String answer2 = "answer2-" + i;
			String answer3 = "answer3-" + i;
			String answer4 = "answer4-" + i;
			QuestionDTO questionDTO = new QuestionDTO(answer1, answer2, answer3, answer4, answer1);
			
			byte [] imageBytes = new byte[] {1,2,3,4,5,6,7,8,9,10};
			
			createQuestion(questionDTO, imageBytes);
		}
	}
		
}
