package com.guesswhat.server.services.rs;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Test;

import com.guesswhat.server.services.rs.dto.RecordDTO;

public class RecordServiceTest extends AbstractServiceTest {
	
	private static final int RECORDS_COUNT = 10;	
	
	@Test
	public void testRecordService() {
		String recordUrl = getRecordUrl();		
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target(recordUrl);
		
		// top records
		List<RecordDTO> records = findTopRecords();
		Assert.assertTrue(records.size() > 1);
		for (int i = 1; i < records.size(); ++i) {
			Assert.assertTrue(records.get(i - 1).getPoints() > records.get(i).getPoints());
		}
		
		// create another records
		Builder invocationBuilder = webTarget.request();
		invocationBuilder.header(HttpHeaders.AUTHORIZATION, getReaderAuthorization());
		for (int i = 0; i < RECORDS_COUNT; ++i) {
			createRecord("user" + (RECORDS_COUNT*2 + i), i);
		}
		
		// user places
		for (int i = 0; i < RECORDS_COUNT; ++i) {
			invocationBuilder =  webTarget.path("place/user" + i).request();
			invocationBuilder.header(HttpHeaders.AUTHORIZATION, getReaderAuthorization());
			Response response = invocationBuilder.post(Entity.entity("", MediaType.APPLICATION_JSON_TYPE));
			Assert.assertEquals(200, response.getStatus());
			
			int userPlace = response.readEntity(Integer.class);
			Assert.assertEquals(RECORDS_COUNT - i, userPlace);
			
			// another users
			invocationBuilder =  webTarget.path("place/user" + (RECORDS_COUNT*2 + i)).request();
			invocationBuilder.header(HttpHeaders.AUTHORIZATION, getReaderAuthorization());
			response = invocationBuilder.post(Entity.entity("", MediaType.APPLICATION_JSON_TYPE));
			Assert.assertEquals(200, response.getStatus());
			
			userPlace = response.readEntity(Integer.class);
			Assert.assertEquals(RECORDS_COUNT - i, userPlace);
		}	
	}
	
	public void createRecords() {
		for (int i = 0; i < RECORDS_COUNT; ++i) {
			createRecord("user" + i, i);
		}
	}
}
