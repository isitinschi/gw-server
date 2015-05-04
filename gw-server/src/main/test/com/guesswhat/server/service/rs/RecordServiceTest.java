package com.guesswhat.server.service.rs;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.guesswhat.server.service.rs.dto.RecordDTO;

public class RecordServiceTest extends AbstractServiceTest {
	
	private static final int RECORDS_COUNT = 10;	
	
	@Test
	public void testRecordService() {
		createRecords();
		
		// top records
		List<RecordDTO> records = findTopRecords();
		Assert.assertTrue(records.size() > 1);
		for (int i = 1; i < records.size(); ++i) {
			Assert.assertTrue(records.get(i - 1).getPoints() > records.get(i).getPoints());
		}
		
		// create another records
		for (int i = 0; i < RECORDS_COUNT; ++i) {
			createRecord("user" + (RECORDS_COUNT*2 + i), i);
		}
		
		// user places
		for (int i = 0; i < RECORDS_COUNT; ++i) {
			int userPlace = findRecordPlace("user" + i);
			Assert.assertEquals(RECORDS_COUNT - i, userPlace);
			
			// another users
			userPlace = findRecordPlace("user" + (RECORDS_COUNT*2 + i));
			Assert.assertEquals(RECORDS_COUNT - i, userPlace);
		}	
	}
	
	private void createRecords() {
		for (int i = 0; i < RECORDS_COUNT; ++i) {
			createRecord("user" + i, i);
		}
	}
}
