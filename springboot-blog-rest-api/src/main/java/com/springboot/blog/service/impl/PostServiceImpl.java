package com.springboot.blog.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.springboot.blog.entity.Category;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.CategoryRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;

@Service
public class PostServiceImpl implements PostService {

	private PostRepository postRepository;
	private ModelMapper mapper;
	private CategoryRepository categoryRepository;
	
	
	
	public PostServiceImpl(PostRepository postRepository, ModelMapper mapper, CategoryRepository categoryRepository) {
		this.postRepository = postRepository;
		this.mapper = mapper;
		this.categoryRepository=categoryRepository;
	}

	@Override
	public PostDto createPost(PostDto postDto) {
		//Convert DTO to entity
//		Post post=new Post();
//		post.setTitle(postDto.getTitle());
//		post.setDescription(postDto.getDescription());
//		post.setContent(postDto.getContent());
		
		Category category= categoryRepository.findById(postDto.getCategoryId())
							.orElseThrow(()->new ResourceNotFoundException("Category", "id", postDto.getCategoryId()));
		
		
		Post post=mapToEntity(postDto);
		post.setCategory(category);
		
		Post newPost=postRepository.save(post);
		
		//convert entity to DTO
		PostDto postResponse=mapToDto(newPost);
		
		return postResponse;
	
	}

	@Override
	public PostResponse getAllPosts(int pageNo, int pageSize,String sortBy,String sortDir) {
		
		Sort sort=sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();
		
		//create Pageable instance
		Pageable pageable=PageRequest.of(pageNo,pageSize,sort);
		
		
		Page<Post> posts=postRepository.findAll(pageable);	
		
		//get content for page object
		List<Post> ListOfPosts=posts.getContent();
		List<PostDto> content= ListOfPosts.stream().map(post->mapToDto(post)).collect(Collectors.toList());
		PostResponse postResponse=new PostResponse();
		postResponse.setContent(content);
		postResponse.setPageNo(posts.getNumber());
		postResponse.setPageSize(posts.getSize());
		postResponse.setTotalElements(posts.getTotalElements());
		postResponse.setTotalPages(posts.getTotalPages());
		postResponse.setLast(posts.isLast());
		
		return postResponse;
	}
	
	
	// Convert entity into DTO
	private PostDto mapToDto(Post post) {
		PostDto postDto=mapper.map(post, PostDto.class);
//		PostDto postDto=new PostDto();
//		postDto.setId(post.getId());
//		postDto.setTitle(post.getTitle());
//		postDto.setDescription(post.getDescription());
//		postDto.setContent(post.getContent());
		return postDto;
	}

	private Post mapToEntity(PostDto postDto) {
		Post post=mapper.map(postDto, Post.class);
//		Post post=new Post();
//		post.setTitle(postDto.getTitle());
//		post.setDescription(postDto.getDescription());
//		post.setContent(postDto.getContent());
		
		return post;
	}

	@Override
	public PostDto getPostById(Long id) {
		Post post=postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post", "id", id));
		return mapToDto(post);
	}

	@Override
	public PostDto updatePost(PostDto postDto, Long id) {
		//Get post by id from the database
		Post post=postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post", "id", id));
		
		Category category= categoryRepository.findById(postDto.getCategoryId())
				.orElseThrow(()->new ResourceNotFoundException("Category", "id", postDto.getCategoryId()));
		post.setTitle(postDto.getTitle());
		post.setDescription(postDto.getDescription());
		post.setContent(postDto.getContent());
		post.setCategory(category);
		
		Post updatedPost=postRepository.save(post);
		return mapToDto(updatedPost);
	}

	@Override
	public void deletePostById(Long id) {
		Post post=postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post", "id", id));
		postRepository.delete(post);
	}

	@Override
	public List<PostDto> getPostByCategory(Long categoryId) {
		categoryRepository.findById(categoryId)
						.orElseThrow(()->new ResourceNotFoundException("Category", "id",categoryId));
		
		List<Post> posts=postRepository.findByCategoryId(categoryId);
		
		
		return posts.stream().map((post)->mapper.map(post,PostDto.class)).collect(Collectors.toList());
	}
}
