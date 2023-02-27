package com.springboot.blog.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

	@Mock
    private PostRepository postRepository;
	
	@Mock
    private ResourceNotFoundException resourceNotFoundException;
	
	@Mock
    private ModelMapper mapper;
    
	@InjectMocks
    private PostService postService = new PostServiceImpl(postRepository, mapper);
	
	private PostDto postDto;
	private Post post;
	private Optional<Post> postOptional;
	
	@BeforeEach
	void setUp() throws Exception {
		//Setting PostDto Object
		postDto = new PostDto();
		postDto.setTitle("Test Title 1");
		postDto.setContent("Test Content 1");
		postDto.setDescription("Test Description 1");
		
		//Setting Post Object
		post = new Post();
		post.setTitle("Test Title 1");
		post.setContent("Test Content 1");
		post.setDescription("Test Description 1");
		postOptional = Optional.of(post);
	}

	@Test
	void testPostServiceImpl() {
		assertNotNull(new PostServiceImpl(postRepository, mapper));
	}

	@Test
	void testCreatePost() {
		
		// Mock the Actual Mapper call (mapToEntity)
		when(mapper.map(postDto, Post.class)).thenReturn(post);
		// Mock the Actual Database call
		when(postRepository.save(post)).thenReturn(post);
		// Mock the Actual Mapper call (mapToEntity)
		when(mapper.map(post, PostDto.class)).thenReturn(postDto);
		
		// Making Original Method Call
		PostDto postResponse = postService.createPost(postDto);
		// Checking Result
		assertEquals("Test Title 1", postResponse.getTitle());
	}

	@Test
	void testGetAllPosts() {
		
		int pageNo = 0;
		int pageSize = 10;
		String sortBy = "id";
		String sortDir = "asc";
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();
		List<Post> listOfPosts = new ArrayList<>();
		// create Pageable instance
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		Page<Post> posts = new PageImpl<Post>(listOfPosts);
		// Mock the Actual Database call (findById)
		when(postRepository.findAll(pageable)).thenReturn(posts);
		postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
	}
	
	@Test
	void testGetAllPosts_desc() {
		
		int pageNo = 0;
		int pageSize = 10;
		String sortBy = "id";
		String sortDir = "desc";
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();
		List<Post> listOfPosts = new ArrayList<>();
		// create Pageable instance
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		Page<Post> posts = new PageImpl<Post>(listOfPosts);
		// Mock the Actual Database call (findById)
		when(postRepository.findAll(pageable)).thenReturn(posts);
		postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
	}

	@Test
	void testGetPostById() {
		// Mock the Actual Database call (findById)
		when(postRepository.findById(Mockito.anyLong())).thenReturn(postOptional);
		
		// Making Original Method Call
		postService.getPostById(Mockito.anyLong());
	}

	@Test
	void testUpdatePost() {
		
		// Mock the Actual Database call (findById)
		when(postRepository.findById(Mockito.anyLong())).thenReturn(postOptional);
		// Mock the Actual Database call
		when(postRepository.save(post)).thenReturn(post);
		// Mock the Actual Mapper call (mapToEntity)
		when(mapper.map(post, PostDto.class)).thenReturn(postDto);
		// Making Original Method Call
		PostDto postResponse = postService.updatePost(postDto, 1L);
		// Checking Result
		assertEquals("Test Title 1", postResponse.getTitle());
	}

	@Test
	void testDeletePostById() {
		
		// Mock the Actual Database call (findById)
		when(postRepository.findById(Mockito.anyLong())).thenReturn(postOptional);
		// Mock the Actual Database call (delete)
		//when(postRepository.delete(post));
		
		// Making Original Method Call
		postService.deletePostById(Mockito.anyLong());
		// Checking Result
		Mockito.verify(postRepository, Mockito.times(1)).delete(post);
		
	}
	
	@Test
	void testDeletePostById_Exception() throws ResourceNotFoundException {
		
		// Mock the Actual Database call (findById)
		when(postRepository.findById(Mockito.anyLong())).thenThrow(ResourceNotFoundException.class);
		//when(resourceNotFoundException.getResourceName()).thenReturn("Post");
		
		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
			postService.deletePostById(Mockito.anyLong());
	    });
	    //assertTrue(exception.getResourceName().equalsIgnoreCase("Post"));
	}
}