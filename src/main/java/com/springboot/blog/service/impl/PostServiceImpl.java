package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Category;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.CategoryRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public PostDto createPost(PostDto postDto) {

        Category category = categoryRepository.findById(postDto.getCategoryId())
                .orElseThrow(()-> new ResourceNotFoundException("Category","id", postDto.getCategoryId()));


        //convert dto to entity
        Post post = mapToEntity(postDto);
        post.setCategory(category);
        Post newPost = postRepository.save(post);
        //convert entity to dto
        PostDto postResponse = mapToDto(newPost);

        return postResponse;
    }

    @Override
    public PostResponse getAllPosts(int pageNo , int pageSize , String sortBy ,String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();

        // pagination
        Pageable pageable = PageRequest.of(pageNo, pageSize , sort);

        /*
         this provides a lot of info about pagination
         page no , page size , total elements , total pages , last post
         */
        Page<Post> posts= postRepository.findAll(pageable);
        // get content
        List<Post> listOfPosts = posts.getContent();
        List<PostDto> content =  listOfPosts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());
        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());
        return postResponse;

    }

    @Override
    public PostDto getPostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Post","id", id));
        System.out.println(post);
        return mapToDto(post);
    }

    @Override
    public PostDto updatePost(PostDto postDto, long Id) {
        //get post by id
        Post post = postRepository.findById(Id).orElseThrow(()->new ResourceNotFoundException("Post","id", Id));
        Category category = categoryRepository.findById(postDto.getCategoryId())
                .orElseThrow(()-> new ResourceNotFoundException("Category","id", postDto.getCategoryId()));


        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setDescription(postDto.getDescription());
        post.setCategory(category);
        Post updatedPost = postRepository.save(post);
        return mapToDto(updatedPost);
    }

    @Override
    public void deletePost(long id) {
        Post post = postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Post","id", id));
        postRepository.delete(post);
    }

    @Override
    public List<PostDto> getPostByCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Category","id",id));

        List<Post> posts  = postRepository.findByCategory(id);

        return posts.stream()
                .map((post)-> mapToDto(post))
                .collect(Collectors.toList());

    }

    private PostDto mapToDto(Post newPost){
        // mapper ->( to convert , PostDto )
        PostDto postDto = mapper.map(newPost, PostDto.class);
        return postDto;
    }
    private Post mapToEntity(PostDto postDto){
        Post post = mapper.map(postDto,Post.class);
        return post;

    }
}
