package com.guesswhat.server.services.rs;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.guesswhat.server.services.rs.dto.QuestionDTO;
import com.guesswhat.server.services.rs.dto.RecordDTO;

public class BackupServiceTest extends AbstractServiceTest {
	
	private static final int QUESTION_COUNT = 10;
	private static final int RECORDS_COUNT = 10;
	
	@Test
	public void testBackup() {
		createQuestions();
		createRecords();
		
		byte [] backup = downloadBackup();
		uploadBackup(backup);
		
		List<QuestionDTO> questions = findQuestions();
		Assert.assertTrue(questions.size() == QUESTION_COUNT);
		
		List<RecordDTO> records = findTopRecords();
		Assert.assertFalse(records.isEmpty());
	}
	
	public void createQuestions() {
		for (int i = 0; i < QUESTION_COUNT; ++i) {
			QuestionDTO questionDTO = new QuestionDTO();
			questionDTO.setAnswer1("answer1-" + i);
			questionDTO.setAnswer2("answer2-" + i);
			questionDTO.setAnswer3("answer3-" + i);
			questionDTO.setAnswer4("answer4-" + i);
			questionDTO.setCorrectAnswer("answer1-" + i);
			
			byte [] imageBytes = new byte[] {1,2,3,4,5};
			
			createQuestion(questionDTO, imageBytes);
		}
	}
	
	public void createRecords() {
		for (int i = 0; i < RECORDS_COUNT; ++i) {
			createRecord("user" + i, i);
		}
	}
}
