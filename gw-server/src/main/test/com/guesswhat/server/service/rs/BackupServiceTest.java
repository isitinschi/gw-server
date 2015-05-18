package com.guesswhat.server.service.rs;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.guesswhat.server.service.rs.dto.ComposedQuestionDTO;
import com.guesswhat.server.service.rs.dto.QuestionDTO;
import com.guesswhat.server.service.rs.dto.RecordDTO;
import com.guesswhat.server.service.rs.dto.RecordDTOListWrapper;

public class BackupServiceTest extends AbstractServiceTest {
	
	private static final int QUESTION_COUNT = 10;
	private static final int RECORDS_COUNT = 10;
	
	@Test
	public void testBackup() {
		createQuestions();
		createRecords();
		
		RecordDTOListWrapper recordBackup = downloadRecordBackup();
		uploadRecordBackup(recordBackup);
		
		List<ComposedQuestionDTO> questionBackup = downloadQuestionBackup();
		uploadQuestionBackup(questionBackup);
		
		List<QuestionDTO> questions = findQuestions();
		Assert.assertTrue(questions.size() == QUESTION_COUNT);
		
		List<RecordDTO> records = findTopRecords();
		Assert.assertFalse(records.isEmpty());
	}
	
	private void createQuestions() {
		for (int i = 0; i < QUESTION_COUNT; ++i) {
			String answer1 = "answer1-" + i;
			String answer2 = "answer2-" + i;
			String answer3 = "answer3-" + i;
			String answer4 = "answer4-" + i;
			QuestionDTO questionDTO = new QuestionDTO(answer1, answer2, answer3, answer4, answer1);
			
			byte [] imageBytes = new byte[] {1,2,3,4,5};
			
			createTestQuestion(questionDTO, imageBytes);
		}
	}
	
	private void createRecords() {
		for (int i = 0; i < RECORDS_COUNT; ++i) {
			createRecord("user" + i, i);
		}
	}
}
