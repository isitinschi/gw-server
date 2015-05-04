package com.guesswhat.server.service.rs.face;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;


public interface BackupService {
	
	@POST
	@Path("/download")
	@Produces("application/octet-stream")
	Response downloadBackup();
	
	@POST
	@Path("/upload")
	@Consumes("application/octet-stream")
	Response uploadBackup(@Context HttpServletRequest request, InputStream fileInputStream);
}
