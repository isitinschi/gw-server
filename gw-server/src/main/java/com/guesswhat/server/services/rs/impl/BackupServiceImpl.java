package com.guesswhat.server.services.rs.impl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

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
import com.guesswhat.server.services.rs.dto.BackupDTO;
import com.guesswhat.server.services.rs.dto.ComposedQuestionDTO;
import com.guesswhat.server.services.rs.dto.ImageDTO;
import com.guesswhat.server.services.rs.dto.QuestionDTO;
import com.guesswhat.server.services.rs.dto.RecordDTO;
import com.guesswhat.server.services.rs.dto.UserDTO;
import com.guesswhat.server.services.rs.face.BackupService;
import com.guesswhat.server.services.rs.face.DatabaseService;
import com.guesswhat.server.services.rs.face.QuestionService;
import com.guesswhat.server.services.security.cfg.UserRole;

@Path("/backup")
public class BackupServiceImpl implements BackupService {

	@Autowired private QuestionService questionService;
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
			List<ComposedQuestionDTO> questionDTOList = new ArrayList<ComposedQuestionDTO>();
			for (Question question : questions) {
				ComposedQuestionDTO composedQuestionDTO = new ComposedQuestionDTO();
				QuestionDTO questionDTO = new QuestionDTO(question);
				composedQuestionDTO.setQuestionDTO(questionDTO);
				
				Long imageHolderQuestionId = question.getImageQuestionId();
				ImageDTO imageDTO = populateImageDTO(imageHolderQuestionId);
				composedQuestionDTO.setImageQuestionDTO(imageDTO);
				
				Long imageHolderAnswerId = question.getImageAnswerId();
				if (imageHolderAnswerId != null) {
					imageDTO = populateImageDTO(imageHolderAnswerId);
					composedQuestionDTO.setImageQuestionDTO(imageDTO);
				}
				
				questionDTOList.add(composedQuestionDTO);
			}
			
			List<User> users = userDAO.findAll();
			List<UserDTO> userDTOList = new ArrayList<UserDTO>();
			for (User user : users) {
				if (!user.getRole().equals(UserRole.ADMIN.toString())) {
					UserDTO userBackupDTO = new UserDTO(user.getUsername(), user.getPassword(), user.getRole());
					userDTOList.add(userBackupDTO);
				}
			}
			
			List<Record> records = recordDAO.findAll();
			List<RecordDTO> recordDTOList = new ArrayList<RecordDTO>();
			for (Record record : records) {
				RecordDTO userBackupDTO = new RecordDTO(record.getUserId(), record.getPoints());
				recordDTOList.add(userBackupDTO);
			}
			
			BackupDTO backup = new BackupDTO();
			backup.setQuestionDTOList(questionDTOList);
			backup.setUserDTOList(userDTOList);
			backup.setRecordDTOList(recordDTOList);
			
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

	private ImageDTO populateImageDTO(Long imageHolderId) {
		ImageDTO imageDTO = new ImageDTO();
		ImageHolder imageHolder = imageHolderDAO.find(imageHolderId);
		imageDTO.setLdpiImageId(getImage(imageHolder.getLdpiImageId()));
		imageDTO.setMdpiImageId(getImage(imageHolder.getMdpiImageId()));
		imageDTO.setHdpiImageId(getImage(imageHolder.getHdpiImageId()));
		imageDTO.setXhdpiImageId(getImage(imageHolder.getXhdpiImageId()));
		imageDTO.setXxhdpiImageId(getImage(imageHolder.getXxhdpiImageId()));
		return imageDTO;
	}

	private byte [] getImage(Long imageId) {
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
		
		return bytes;
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
		
		for (ComposedQuestionDTO composedQuestionDTO : backupDTO.getQuestionDTOList()) {
			questionService.createQuestion(composedQuestionDTO);
		}
		
		for (RecordDTO recordBackupDTO : backupDTO.getRecordDTOList()) {
			Record record = new Record(recordBackupDTO.getUserId(), recordBackupDTO.getPoints());
			recordDAO.save(record);
		}
		
		for (UserDTO userBackupDTO : backupDTO.getUserDTOList()) {
			User user = new User(userBackupDTO.getUsername(), userBackupDTO.getPassword(), userBackupDTO.getRole());
			if (!user.getRole().equals(UserRole.ADMIN.toString())) {
				userDAO.save(user);
			}
		}
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

