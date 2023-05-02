package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.BlogApiException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl  implements CommentService {
    private ModelMapper mapper;
    private CommentRepository commentRepository;

    private PostRepository postRepository;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository , ModelMapper mapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository ;
        this.mapper = mapper;

    }
    private  CommentDto mapToDto(Comment comment){
        CommentDto commentDto = mapper.map(comment ,CommentDto.class);
        return commentDto;
    }
    private  Comment mapToEntity(CommentDto comment){
        Comment commentDto = mapper.map(comment, Comment.class);
        return commentDto;
    }
    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        Comment comment = mapToEntity(commentDto);
        // retrieve post entity by id ;
        Post post= postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post", "id", postId));
        // set post to comment entity
        comment.setPost(post);

        // save the comment entity
        Comment newComment = commentRepository.save(comment);

        return mapToDto(newComment);
    }

    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {
        // retrieve comments by postId ;
        List<Comment> comment = commentRepository.findByPostId(postId);
        // convert to dto and then to list

        return comment.stream().map(comment1 -> mapToDto(comment1)).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(long commentId ,long postId) {
        Post post= postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post", "id", postId));

        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment", "id", commentId));
        long currpostId = comment.getPost().getId();
        if(currpostId != post.getId()) {
               throw new BlogApiException(HttpStatus.BAD_REQUEST , "POST ID NOT MATCHING");
        }
        return mapToDto(comment);



    }

    @Override
    public CommentDto updateComment(Long postId, long commentId, CommentDto commentRequest) {
        Post post= postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post", "id", postId));

        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment", "id", commentId));
        long currpostId = comment.getPost().getId();
        if(currpostId != post.getId()) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST , "POST ID NOT MATCHING");
        }
        comment.setBody(commentRequest.getBody());
        comment.setName(commentRequest.getName());
        comment.setEmail(commentRequest.getEmail());
        Comment  newComment = commentRepository.save(comment);
        return mapToDto(newComment);

    }

    @Override
    public void deleteComment(Long postId, Long commentId) {
        Post post= postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post", "id", postId));

        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment", "id", commentId));
        long currpostId = comment.getPost().getId();
        if(currpostId != post.getId()) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST , "POST ID NOT MATCHING");
        }
        commentRepository.delete(comment);
        return ;
    }
}
