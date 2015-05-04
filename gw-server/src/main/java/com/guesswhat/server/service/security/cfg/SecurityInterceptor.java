package com.guesswhat.server.service.security.cfg;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.io.BaseEncoding;
import com.guesswhat.server.persistence.jpa.dao.UserDAO;
import com.guesswhat.server.persistence.jpa.entity.User;

@Provider
public class SecurityInterceptor implements javax.ws.rs.container.ContainerRequestFilter {
    private static final String AUTHORIZATION_PROPERTY = "Authorization";
    private static final String AUTHENTICATION_SCHEME = "Basic";
    
    @Context
    private ResourceInfo resourceInfo;
    
    @Autowired private UserDAO userDAO;
    
    @Override
    public void filter(ContainerRequestContext requestContext) {
        Method method = resourceInfo.getResourceMethod();
        //Access allowed for all
        if ( ! method.isAnnotationPresent(PermitAll.class)) {
            //Access denied for all
            if (method.isAnnotationPresent(DenyAll.class)) {
                requestContext.abortWith(Response.status(Status.FORBIDDEN).build());
                return;
            }
             
            //Get request headers
            final MultivaluedMap<String, String> headers = requestContext.getHeaders();
             
            //Fetch authorization header
            final List<String> authorization = headers.get(AUTHORIZATION_PROPERTY);
             
            //If no authorization information present; block access
            if (authorization == null || authorization.isEmpty()) {
                requestContext.abortWith(Response.status(Status.UNAUTHORIZED).build());
                return;
            }
             
            //Get encoded username and password
            final String encodedUserPassword = authorization.get(0).replaceFirst(AUTHENTICATION_SCHEME + " ", "");
             
            //Decode username and password
            String usernameAndPassword = null;
            usernameAndPassword = new String(BaseEncoding.base64().decode(encodedUserPassword));
             
            //Split username and password tokens
            final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
            final String username = tokenizer.nextToken();
            final String password = tokenizer.nextToken();
             
            //Verifying Username and password
            System.out.println(username);
            System.out.println(password);
             
            //Verify user access
            if (method.isAnnotationPresent(RolesAllowed.class)) {
                RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
                Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));
                 
                //Is user valid?
                if (!isUserAllowed(username, password, rolesSet)) {
                    requestContext.abortWith(Response.status(Status.UNAUTHORIZED).build());
                    return;
                }
            }
        }
    }
    
    private boolean isUserAllowed(final String username, final String password, final Set<String> rolesSet) {
    	// Authentication
    	boolean authenticated = false;
        User user = userDAO.findUser(username);
        if (user != null) {        	        
	        if (user.getRole().equals(UserRole.READER.toString())) {
	        	if (user.getPassword().equals(password)) {
	        		authenticated = true;
	        	}
	        } else {
	        	try {
					authenticated = SecuredField.check(password, user.getPassword());
				} catch (Exception e) {
					authenticated  = false;
				}
	        }
        }
        
        if (!authenticated) {
        	return false;
        }
        
        // Authorization
        boolean authorized = false;         
        String userRole = user.getRole();
        if (rolesSet.contains(userRole)) {
        	authorized = true;
        }
        return authorized;
    }
     
}