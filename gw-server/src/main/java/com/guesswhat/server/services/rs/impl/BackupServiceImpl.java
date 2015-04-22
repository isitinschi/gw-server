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

import org.springframework.beans.factory.annotation.Autowired;

import com.guesswhat.server.persistence.jpa.dao.ImageDAO;
import com.guesswhat.server.persistence.jpa.dao.ImageHolderDAO;
import com.guesswhat.server.persistence.jpa.dao.QuestionDAO;
import com.guesswhat.server.persistence.jpa.dao.RecordDAO;
import com.guesswhat.server.persistence.jpa.dao.UserDAO;
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
import com.guesswhat.server.services.rs.face.DatabaseService;
import com.guesswhat.server.services.rs.face.ImageService;
import com.guesswhat.server.services.security.cfg.UserRole;

@Path("/backup")
public class BackupServiceImpl implements BackupService {

	@Autowired private ImageService imageService;
	@Autowired private DatabaseService databaseService;
	
	@Autowired private QuestionDAO questionDAO;
	@Autowired private ImageHolderDAO imageHolderDAO;
	@Autowired private ImageDAO imageDAO;
	@Autowired private UserDAO userDAO;
	@Autowired private RecordDAO recordDAO;
	
	@Override
	@RolesAllowed("WRITER")
	public Response downloadBackup() {		
		try {			
			List<Question> questions = questionDAO.findAll();
			List<QuestionBackupDTO> questionBackupDTOList = new ArrayList<QuestionBackupDTO>();
			for (Question question : questions) {
				QuestionBackupDTO questionBackupDTO = new QuestionBackupDTO(question);
				
				Long imageHolderQuestionId = question.getImageQuestionId();
				populateImages(questionBackupDTO.getImageBackupDTOQuestionMap(), imageHolderQuestionId);
				
				Long imageHolderAnswerId = question.getImageAnswerId();
				if (imageHolderAnswerId != null) {
					populateImages(questionBackupDTO.getImageBackupDTOAnswerMap(), imageHolderAnswerId);
				}
				
				questionBackupDTOList.add(questionBackupDTO);
			}
			
			List<User> users = userDAO.findAll();
			List<UserBackupDTO> userBackupDTOList = new ArrayList<UserBackupDTO>();
			for (User user : users) {
				if (!user.getRole().equals(UserRole.ADMIN.toString())) {
					UserBackupDTO userBackupDTO = new UserBackupDTO(user.getUsername(), user.getPassword(), user.getRole());
					userBackupDTOList.add(userBackupDTO);
				}
			}
			
			List<Record> records = recordDAO.findAll();
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

	private void populateImages(Map<ImageType, ImageBackupDTO> imageBackupDTOList, Long imageHolderId) {
		ImageHolder imageHolder = imageHolderDAO.find(imageHolderId);
		imageBackupDTOList.put(ImageType.LDPI, getImage(imageHolder.getLdpiImageId()));
		imageBackupDTOList.put(ImageType.MDPI, getImage(imageHolder.getMdpiImageId()));
		imageBackupDTOList.put(ImageType.HDPI, getImage(imageHolder.getHdpiImageId()));
		imageBackupDTOList.put(ImageType.XHDPI, getImage(imageHolder.getXhdpiImageId()));
		imageBackupDTOList.put(ImageType.XXHDPI, getImage(imageHolder.getXxhdpiImageId()));
	}

	private ImageBackupDTO getImage(Long imageId) {
		Image image = imageDAO.find(imageId);
		Image imageSecondPart = null;
		if (image.getSecondPart() != null) {
			imageSecondPart = imageDAO.find(image.getSecondPart());
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
				databaseService.incrementVersion();
				
				return Response.ok().build();
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		return Response.serverError().build();
	}

	private void uploadBackup(BackupDTO backupDTO) {
		User admin = userDAO.findAdmin();
		List<User> users = userDAO.findAll();
		users.remove(admin);
		userDAO.removeAll(users);
		
		recordDAO.removeAll();
		questionDAO.removeAll();
		imageHolderDAO.removeAll();
		imageDAO.removeAll();
		
		for (QuestionBackupDTO questionBackupDTO : backupDTO.getQuestionBackupDTOList()) {
			Question question = new Question(questionBackupDTO);
			
			ImageHolder imageQuestionHolder = new ImageHolder();
			for (ImageType imageType : questionBackupDTO.getImageBackupDTOQuestionMap().keySet()) {
				ImageBackupDTO imageBackupDTO = questionBackupDTO.getImageBackupDTOQuestionMap().get(imageType);
				InputStream inputStream = new ByteArrayInputStream(imageBackupDTO.getBytes()); 
				imageService.buildImageHolder(imageQuestionHolder, imageType.toString(), inputStream);
			}
			
			ImageHolder imageAnswerHolder = new ImageHolder();
			for (ImageType imageType : questionBackupDTO.getImageBackupDTOAnswerMap().keySet()) {
				ImageBackupDTO imageBackupDTO = questionBackupDTO.getImageBackupDTOQuestionMap().get(imageType);
				InputStream inputStream = new ByteArrayInputStream(imageBackupDTO.getBytes()); 
				imageService.buildImageHolder(imageAnswerHolder, imageType.toString(), inputStream);
			}
			
			if (imageQuestionHolder.isFull()) {
				imageHolderDAO.save(imageQuestionHolder);
				question.setImageQuestionId(imageQuestionHolder.getId());
				if (imageAnswerHolder.isFull()) {
					imageHolderDAO.save(imageAnswerHolder);
					question.setImageAnswerId(imageAnswerHolder.getId());
				}
				questionDAO.save(question);
			}
		}
		
		for (RecordBackupDTO recordBackupDTO : backupDTO.getRecordBackupDTOList()) {
			Record record = new Record(recordBackupDTO.getUserId(), recordBackupDTO.getPoints());
			recordDAO.save(record);
		}
		
		for (UserBackupDTO userBackupDTO : backupDTO.getUserBackupDTOList()) {
			User user = new User(userBackupDTO.getUsername(), userBackupDTO.getPassword(), userBackupDTO.getRole());
			if (!user.getRole().equals(UserRole.ADMIN.toString())) {
				userDAO.save(user);
			}
		}
	}

	public void setImageService(ImageService imageService) {
		this.imageService = imageService;
	}

	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}

	public void setQuestionDAO(QuestionDAO questionDAO) {
		this.questionDAO = questionDAO;
	}

	public void setImageHolderDAO(ImageHolderDAO imageHolderDAO) {
		this.imageHolderDAO = imageHolderDAO;
	}

	public void setImageDAO(ImageDAO imageDAO) {
		this.imageDAO = imageDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public void setRecordDAO(RecordDAO recordDAO) {
		this.recordDAO = recordDAO;
	}
	
}

