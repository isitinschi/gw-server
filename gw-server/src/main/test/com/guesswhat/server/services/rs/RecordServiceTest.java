package com.guesswhat.server.services.rs;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Test;

import com.guesswhat.server.services.rs.dto.RecordDTO;

public class RecordServiceTest extends AbstractServiceTest {
	
	private static final int RECORDS_COUNT = 5;

	@Test
	public void testRecordService() {
		String recordUrl = getRecordUrl();
		
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target(recordUrl);
		Response response = null;
		
		// create records
		Builder invocationBuilder = webTarget.request();
		invocationBuilder.header(HttpHeaders.AUTHORIZATION, getReaderAuthorization());
		for (int i = 0; i < RECORDS_COUNT; ++i) {
			RecordDTO recordDTO = new RecordDTO();
			recordDTO.setUserId("user" + i);
			recordDTO.setPoints(i);
			
			response = invocationBuilder.put(Entity.entity(recordDTO, MediaType.APPLICATION_JSON_TYPE));		
			Assert.assertEquals(200, response.getStatus());
		}		
		
		// top records
		invocationBuilder = webTarget.path("top").request();
		invocationBuilder.header(HttpHeaders.AUTHORIZATION, getReaderAuthorization());
		response = invocationBuilder.accept(MediaType.APPLICATION_JSON).post(Entity.entity("", MediaType.APPLICATION_JSON_TYPE));
		Assert.assertEquals(200, response.getStatus());
		List<RecordDTO> records = response.readEntity(new GenericType<List<RecordDTO>>() {});
		Assert.assertNotNull(records);
		Assert.assertTrue(records.size() > 1);
		for (int i = 1; i < records.size(); ++i) {
			Assert.assertTrue(records.get(i - 1).getPoints() > records.get(i).getPoints());
		}
		
		// create another records
		invocationBuilder = webTarget.request();
		invocationBuilder.header(HttpHeaders.AUTHORIZATION, getReaderAuthorization());
		for (int i = 0; i < RECORDS_COUNT; ++i) {
			RecordDTO recordDTO = new RecordDTO();
			recordDTO.setUserId("user" + (RECORDS_COUNT*2 + i));
			recordDTO.setPoints(i);
			
			response = invocationBuilder.put(Entity.entity(recordDTO, MediaType.APPLICATION_JSON_TYPE));		
			Assert.assertEquals(200, response.getStatus());
		}
		
		// user places
		for (int i = 0; i < RECORDS_COUNT; ++i) {
			invocationBuilder =  webTarget.path("place/user" + i).request();
			invocationBuilder.header(HttpHeaders.AUTHORIZATION, getReaderAuthorization());
			response = invocationBuilder.post(Entity.entity("", MediaType.APPLICATION_JSON_TYPE));
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
}
