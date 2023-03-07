package com.ibm.webdev.app.jersey.start.filter;

import java.io.IOException;
import java.security.spec.InvalidKeySpecException;

import javax.annotation.Priority;
import javax.security.sasl.AuthenticationException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;

import org.apache.tomcat.util.codec.binary.Base64;

import com.ibm.webdev.app.jersey.annotation.Secured;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFliter implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		String authoriztionHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		if (authoriztionHeader == null || !authoriztionHeader.startsWith("Bearer")) {
			throw new AuthenticationException("Authoriztion Header must be provided !");
		}
		String token = authoriztionHeader.substring("Bearer".length()).trim();
		String userId = requestContext.getUriInfo().getPathParameters().getFirst("id");
		
		validateToken(token, userId);
	}
	
	private void validateToken(String token, String userId) throws AuthenticationException {
//		UserService userService = new UserServiceImpl();
//		UserDTO userProfile = userService.getUserById(userId);
//		
//		String completeToken = userProfile.getToken() + token;
//		
//		String securePassword = userProfile.getEncryptedPassword();
//		String salt = userProfile.getSalt();
//		String accessTokentMaterial = securePassword + salt;
//		byte[] encryptedAccessToken = null;
//		try {
//			encryptedAccessToken = new UserProfileUtils().encrypte(securePassword, accessTokentMaterial);
//		}
//		catch(InvalidKeySpecException ex) {
//			throw new AuthenticationException("Faled to issue secure access token !");
//		}
//		String encryptedAccessTokenBase64Encoded = Base64.encodeBase64String(encryptedAccessToken);
//		
//		if(!encryptedAccessTokenBase64Encoded.equalsIgnoreCase(completeToken)) {
//			throw new AuthenticationException("Authorization token did not match !");
//		}
	}
}
