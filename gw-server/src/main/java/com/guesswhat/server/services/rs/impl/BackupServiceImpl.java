package com.guesswhat.server.services.rs.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.google.appengine.api.datastore.Key;
import com.guesswhat.server.persistence.jpa.cfg.EntityFactory;
import com.guesswhat.server.persistence.jpa.entity.Image;
import com.guesswhat.server.persistence.jpa.entity.ImageHolder;
import com.guesswhat.server.persistence.jpa.entity.Question;
import com.guesswhat.server.persistence.jpa.entity.Record;
import com.guesswhat.server.persistence.jpa.entity.User;
import com.guesswhat.server.services.rs.backup.dto.BackupDTO;
import com.guesswhat.server.services.rs.backup.dto.ImageBackupDTO;
import com.guesswhat.server.services.rs.backup.dto.QuestionBackupDTO;
import com.guesswhat.server.services.rs.backup.dto.RecordBackupDTO;
import com.guesswhat.server.services.rs.backup.dto.UserBackupDTO;
import com.guesswhat.server.services.rs.dto.ImageType;
import com.guesswhat.server.services.rs.face.BackupService;
import com.guesswhat.server.services.security.cfg.UserRole;

@Path("/backup")
public class BackupServiceImpl implements BackupService {

	@Override
	@RolesAllowed("WRITER")
	public Response downloadBackup() {		
		try {			
			List<Question> questions = EntityFactory.getInstance().getQuestionDAO().findAll();
			List<QuestionBackupDTO> questionBackupDTOList = new ArrayList<QuestionBackupDTO>();
			for (Question question : questions) {
				QuestionBackupDTO questionBackupDTO = new QuestionBackupDTO(question);
				
				Key imageHolderQuestionKey = question.getImageQuestion();
				populateImages(questionBackupDTO.getImageBackupDTOQuestionMap(), imageHolderQuestionKey);
				
				Key imageHolderAnswerKey = question.getImageAnswer();
				if (imageHolderAnswerKey != null) {
					populateImages(questionBackupDTO.getImageBackupDTOAnswerMap(), imageHolderAnswerKey);
				}
				
				questionBackupDTOList.add(questionBackupDTO);
			}
			
			List<User> users = EntityFactory.getInstance().getUserDAO().findAll();
			List<UserBackupDTO> userBackupDTOList = new ArrayList<UserBackupDTO>();
			for (User user : users) {
				if (!user.getRole().equals(UserRole.ADMIN.toString())) {
					UserBackupDTO userBackupDTO = new UserBackupDTO(user.getUsername(), user.getPassword(), user.getRole());
					userBackupDTOList.add(userBackupDTO);
				}
			}
			
			List<Record> records = EntityFactory.getInstance().getRecordDAO().findAll();
			List<RecordBackupDTO> recordBackupDTOList = new ArrayList<RecordBackupDTO>();
			for (Record record : records) {
				RecordBackupDTO userBackupDTO = new RecordBackupDTO(record.getUserId(), record.getPoints());
				recordBackupDTOList.add(userBackupDTO);
			}
			
			BackupDTO backup = new BackupDTO();
			backup.setQuestionBackupDTOList(questionBackupDTOList);
			backup.setUserBackupDTOList(userBackupDTOList);
			backup.setRecordBackupDTOList(recordBackupDTOList);
			
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(buffer);
			oos.writeObject(backup);
			oos.flush();
			oos.close();
			
			return Response.ok(buffer.toByteArray()).build();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return Response.serverError().build();
	}

	private void populateImages(Map<ImageType, ImageBackupDTO> imageBackupDTOList, Key imageHolderKey) {
		ImageHolder imageHolder = EntityFactory.getInstance().getImageHolderDAO().find(imageHolderKey);
		imageBackupDTOList.put(ImageType.LDPI, getImage(imageHolder.getLdpiImage()));
		imageBackupDTOList.put(ImageType.MDPI, getImage(imageHolder.getMdpiImage()));
		imageBackupDTOList.put(ImageType.HDPI, getImage(imageHolder.getHdpiImage()));
		imageBackupDTOList.put(ImageType.XHDPI, getImage(imageHolder.getXhdpiImage()));
		imageBackupDTOList.put(ImageType.XXHDPI, getImage(imageHolder.getXxhdpiImage()));
	}

	private ImageBackupDTO getImage(Key imageKey) {
		Image image = EntityFactory.getInstance().getImageDAO().find(imageKey);
		Image imageSecondPart = null;
		if (image.getSecondPart() != null) {
			imageSecondPart = EntityFactory.getInstance().getImageDAO().find(image.getSecondPart());
		}
		
		byte [] bytes = null;
		if (imageSecondPart != null) {
			bytes = concat(image.getImage().getBytes(), imageSecondPart.getImage().getBytes());
		} else {
			bytes = image.getImage().getBytes();
		}
		
		return new ImageBackupDTO(bytes);
	}
	
	private byte[] concat(byte[] a, byte[] b) {
		   int aLen = a.length;
		   int bLen = b.length;
		   byte[] c= new byte[aLen+bLen];
		   System.arraycopy(a, 0, c, 0, aLen);
		   System.arraycopy(b, 0, c, aLen, bLen);
		   return c;
	}

	@Override
	@RolesAllowed("WRITER")
	public Response uploadBackup(HttpServletRequest request, InputStream fileInputStream) {
		try {
			ObjectInputStream ois = new ObjectInputStream(fileInputStream);
			Object obj = ois.readObject();
			if (obj instanceof BackupDTO) {
				BackupDTO backupDTO = (BackupDTO) obj;
				uploadBackup(backupDTO);
				DatabaseServiceImpl.incrementVersion();
				
				return Response.ok().build();
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		return Response.serverError().build();
	}

	private void uploadBackup(BackupDTO backupDTO) {
		EntityFactory.getInstance().getQuestionDAO().removeAll();
		User admin = EntityFactory.getInstance().getUserDAO().findAdmin();
		List<User> users = EntityFactory.getInstance().getUserDAO().findAll();
		users.remove(admin);
		EntityFactory.getInstance().getUserDAO().removeAll(users);
		
		EntityFactory.getInstance().getImageHolderDAO().removeAll();
		EntityFactory.getInstance().getRecordDAO().removeAll();
		EntityFactory.getInstance().getImageDAO().removeAll();
		
		for (QuestionBackupDTO questionBackupDTO : backupDTO.getQuestionBackupDTOList()) {
			Question question = new Question(questionBackupDTO);
			
			ImageHolder imageQuestionHolder = new ImageHolder();
			for (ImageType imageType : questionBackupDTO.getImageBackupDTOQuestionMap().keySet()) {
				ImageBackupDTO imageBackupDTO = questionBackupDTO.getImageBackupDTOQuestionMap().get(imageType);
				InputStream inputStream = new ByteArrayInputStream(imageBackupDTO.getBytes()); 
				ImageServiceImpl.buildImageHolder(imageQuestionHolder, imageType.toString(), inputStream);
			}
			
			ImageHolder imageAnswerHolder = new ImageHolder();
			for (ImageType imageType : questionBackupDTO.getImageBackupDTOAnswerMap().keySet()) {
				ImageBackupDTO imageBackupDTO = questionBackupDTO.getImageBackupDTOQuestionMap().get(imageType);
				InputStream inputStream = new ByteArrayInputStream(imageBackupDTO.getBytes()); 
				ImageServiceImpl.buildImageHolder(imageAnswerHolder, imageType.toString(), inputStream);
			}
			
			if (imageQuestionHolder.isFull()) {
				EntityFactory.getInstance().getImageHolderDAO().save(imageQuestionHolder);
				question.setImageQuestion(imageQuestionHolder.getKey());
				if (imageAnswerHolder.isFull()) {
					EntityFactory.getInstance().getImageHolderDAO().save(imageAnswerHolder);
					question.setImageAnswer(imageAnswerHolder.getKey());
				}
				EntityFactory.getInstance().getQuestionDAO().save(question);
			}
		}
		
		for (RecordBackupDTO recordBackupDTO : backupDTO.getRecordBackupDTOList()) {
			Record record = new Record(recordBackupDTO.getUserId(), recordBackupDTO.getPoints());
			EntityFactory.getInstance().getRecordDAO().save(record);
		}
		
		for (UserBackupDTO userBackupDTO : backupDTO.getUserBackupDTOList()) {
			User user = new User(userBackupDTO.getUsername(), userBackupDTO.getPassword(), userBackupDTO.getRole());
			if (!user.getRole().equals(UserRole.ADMIN.toString())) {
				EntityFactory.getInstance().getUserDAO().save(user);
			}
		}
	}

}

