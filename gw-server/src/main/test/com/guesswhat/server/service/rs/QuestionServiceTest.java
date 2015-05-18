package com.guesswhat.server.service.rs;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.guesswhat.server.service.rs.dto.ImageType;
import com.guesswhat.server.service.rs.dto.QuestionDTO;

public class QuestionServiceTest extends AbstractServiceTest {
	
	private static final int QUESTIONS_COUNT = 10;
	
	@Test
	public void testQuestions() {
		createQuestions();
		
		List<QuestionDTO> questions = findQuestions();		
		Assert.assertTrue(questions.size() == QUESTIONS_COUNT);
		
		for (int i = 0; i < QUESTIONS_COUNT; ++i) {
			QuestionDTO questionDTO = questions.get(i);
			
			Assert.assertTrue(questionDTO.getAnswer1().contains("answer1-"));
			Assert.assertTrue(questionDTO.getAnswer2().contains("answer2-"));
			Assert.assertTrue(questionDTO.getAnswer3().contains("answer3-"));
			Assert.assertTrue(questionDTO.getAnswer4().contains("answer4-"));
			Assert.assertTrue(questionDTO.getCorrectAnswer().contains("answer1-"));
			
			testQuestionImages(questionDTO.getId());
		}
	}
	
	public void testQuestionImages(String questionId) {
		for (ImageType type : ImageType.values()) {
			testImage(questionId, type, "question");
			testImage(questionId, type, "answer");
		}		
	}

	private void testImage(String questionId, ImageType type, String path) {
		byte[] bytes = downloadImage(questionId, type, path);
		
		if (path.equals("answer") && (bytes == null || bytes.length == 0)) {
			return; // sometimes, there is no answer ;-)
		}
		
		Assert.assertNotNull(bytes);
		Assert.assertTrue(bytes.length == 5);
		for (int i = 1; i <= 5; ++i) {
			Assert.assertEquals(i, (int) Integer.valueOf(bytes[i - 1]));
		}
	}
	
	private void createQuestions() {
		for (int i = 0; i < QUESTIONS_COUNT; ++i) {
			String answer1 = "answer1-" + i;
			String answer2 = "answer2-" + i;
			String answer3 = "answer3-" + i;
			String answer4 = "answer4-" + i;
			QuestionDTO questionDTO = new QuestionDTO(answer1, answer2, answer3, answer4, answer1);
			
			byte [] imageBytes = new byte[] {1,2,3,4,5};
			
			createTestQuestion(questionDTO, imageBytes);
		}
	}
}
