package com.springboot.blog.payload;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class PostDto {
    private  long id;
    @NotEmpty
    @Size(min =2 , message = "Post  title should have atleast 2 characters")
    private String title ;
    @NotEmpty
    private String content;
    @NotEmpty
    @Size(min =10 , message = "Post description should have atleast 2 characters")
    private  String description;
    private Set<CommentDto> comments;
    private Long categoryId ;


}
