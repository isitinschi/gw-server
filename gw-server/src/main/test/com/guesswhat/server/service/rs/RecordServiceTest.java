package com.guesswhat.server.service.rs;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
	
	private int findRecordPlace(String userId) {
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target(getRecordUrl());

		Builder invocationBuilder = webTarget.request();		
		invocationBuilder =  webTarget.path("place").path(userId).request();
		invocationBuilder.header(HttpHeaders.AUTHORIZATION, getReaderAuthorization());
		Response response = invocationBuilder.post(Entity.entity("", MediaType.APPLICATION_JSON_TYPE));
		Assert.assertEquals(200, response.getStatus());
		
		int userPlace = response.readEntity(Integer.class);
		
		return userPlace;
	}
	
}
