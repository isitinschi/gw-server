package com.guesswhat.server.service.rs.impl;

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
import com.guesswhat.server.persistence.jpa.entity.ImageHolder;
import com.guesswhat.server.persistence.jpa.entity.Question;
import com.guesswhat.server.persistence.jpa.entity.Record;
import com.guesswhat.server.persistence.jpa.entity.User;
import com.guesswhat.server.service.rs.dto.BackupDTO;
import com.guesswhat.server.service.rs.dto.ComposedQuestionDTO;
import com.guesswhat.server.service.rs.dto.ImageDTO;
import com.guesswhat.server.service.rs.dto.QuestionDTO;
import com.guesswhat.server.service.rs.dto.RecordDTO;
import com.guesswhat.server.service.rs.dto.UserDTO;
import com.guesswhat.server.service.rs.face.BackupService;
import com.guesswhat.server.service.rs.face.DatabaseService;
import com.guesswhat.server.service.rs.face.ImageService;
import com.guesswhat.server.service.rs.face.QuestionService;
import com.guesswhat.server.service.security.cfg.UserRole;

@Path("/backup")
public class BackupServiceImpl implements BackupService {

	@Autowired private QuestionService questionService;
	@Autowired private DatabaseService databaseService;
	@Autowired private ImageService imageService;
	
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return Response.serverError().build();
	}

	private ImageDTO populateImageDTO(Long imageHolderId) {
		ImageDTO imageDTO = new ImageDTO();
		ImageHolder imageHolder = imageHolderDAO.find(imageHolderId);
		imageDTO.setLdpiImageId(imageService.findImageById(imageHolder.getLdpiImageId()));
		imageDTO.setMdpiImageId(imageService.findImageById(imageHolder.getMdpiImageId()));
		imageDTO.setHdpiImageId(imageService.findImageById(imageHolder.getHdpiImageId()));
		imageDTO.setXhdpiImageId(imageService.findImageById(imageHolder.getXhdpiImageId()));
		imageDTO.setXxhdpiImageId(imageService.findImageById(imageHolder.getXxhdpiImageId()));
		return imageDTO;
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

