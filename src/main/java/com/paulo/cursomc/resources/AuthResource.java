package com.paulo.cursomc.resources;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.paulo.cursomc.dto.EmailDTO;
import com.paulo.cursomc.security.JWTUtil;
import com.paulo.cursomc.security.UserSS;
import com.paulo.cursomc.services.AuthService;
import com.paulo.cursomc.services.UserService;

@RestController
@RequestMapping(value="/auth")
public class AuthResource {
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private AuthService authService;
	
	/**
	 * Para renovar o o token
	 * @param HttpServletResponse - Para poder pegar o TOKEN
	 * @return ResponseEntity - Retorna o novo TOKEN
	 */
	@RequestMapping(value="/refresh_token",method=RequestMethod.POST)
	public ResponseEntity<Void> refreshToken(HttpServletResponse response){
		UserSS user = UserService.authenticated();
		String token = jwtUtil.generateToken(user.getUsername());
		response.addHeader("Autorization", "Bearer " + token);
		response.addHeader("access-control-expose-headers", "Authorization");
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(value="/forgot",method=RequestMethod.POST)
	public ResponseEntity<Void> forgot(@Valid @RequestBody EmailDTO objdto){
		authService.sendNewPassword(objdto.getEmail());
		return ResponseEntity.noContent().build();
	}
}
