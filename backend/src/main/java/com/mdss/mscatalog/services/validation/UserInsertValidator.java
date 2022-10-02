package com.mdss.mscatalog.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.mdss.mscatalog.dto.UserInsertDto;
import com.mdss.mscatalog.entities.User;
import com.mdss.mscatalog.repositories.UserRepository;
import com.mdss.mscatalog.resources.exceptions.FieldMessage;

public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDto> {
	
	@Autowired
	private UserRepository repository;
	
	@Override
	public void initialize(UserInsertValid ann) {
	}

	@Override
	public boolean isValid(UserInsertDto dto, ConstraintValidatorContext context) {
		
		List<FieldMessage> list = new ArrayList<>();
		
		User user = repository.findByEmail(dto.getEmail());
		if(user != null) {
			list.add(new FieldMessage("Email","Email: "+ dto.getEmail() +" already exists!"));
		}
		
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}