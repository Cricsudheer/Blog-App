package com.springboot.blog.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private long id ;
    @NotEmpty(message = "name should not be null")
    private String name ;
    @NotEmpty(message = "EMAIL SHOULD not be null")
    @Email
    private String email ;
    @NotEmpty
    @Size(min =10 , message = "comment body must be min 10 char")
    private String body ;
}
