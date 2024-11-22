package com.springboot.blog.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class CommentDto {
	
	private Long id;
	
	//name should not be empty or null
	@NotEmpty(message="Name should not be null or empty")
	private String name;
	
	//email should not be null or empty
	//email field validation
	@NotEmpty(message="Email should not be null or empty")
	@Email
	private String email;
	
	//Comment body should not be null or empty
	//Comment body must be minimum 10 characters
	@NotEmpty
	@Size(min=10, message="Comment body must be minimum 10 characters")
	private String body;
   // Added comment
}
