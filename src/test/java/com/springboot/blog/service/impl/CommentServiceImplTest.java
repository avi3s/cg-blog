package com.springboot.blog.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

	@Mock
    private PostRepository postRepository;
	
	@Mock
    private CommentRepository commentRepository;
	
	@Mock
    private ResourceNotFoundException resourceNotFoundException;
	
	@Mock
    private ModelMapper mapper;
	
	@InjectMocks
    private CommentService commentService = new CommentServiceImpl(commentRepository, postRepository, mapper);
	
	private CommentDto commentDto;
	private Comment comment;
	private Post post;
	private Optional<Post> postOptional;
	private Optional<Comment> commentOptional;
	
	@BeforeEach
	void setUp() throws Exception {
		//Setting CommentDto Object
		commentDto = new CommentDto();
		commentDto.setBody("Test Body");
		commentDto.setEmail("avirup.pal@capgemini.com");
		commentDto.setName("Test Comment");
		//Setting Comment Object
		comment = new Comment();
		comment.setBody(commentDto.getBody());
		comment.setEmail(commentDto.getEmail());
		comment.setName(commentDto.getName());
		//Setting Post Object
		post = new Post();
		post.setId(1L);
		post.setTitle("Test Title 1");
		post.setContent("Test Content 1");
		post.setDescription("Test Description 1");
		comment.setPost(post);
		postOptional = Optional.of(post);
		commentOptional = Optional.of(comment);
	}

	@Test
	void testCommentServiceImpl() {
		assertNotNull(new CommentServiceImpl(commentRepository, postRepository, mapper));
	}

	@Test
	void testCreateComment() {
		
		// Mock the Actual Database call (findById)
		when(postRepository.findById(Mockito.anyLong())).thenReturn(postOptional);
		// Mock the Actual Mapper call (mapToEntity)
  		when(mapper.map(commentDto, Comment.class)).thenReturn(comment);
  		// Mock the Actual Database call
  		when(commentRepository.save(comment)).thenReturn(comment);
  		// Mock the Actual Mapper call (mapToEntity)
  		when(mapper.map(comment, CommentDto.class)).thenReturn(commentDto);
  		// Making Original Method Call
  		CommentDto commentResponse = commentService.createComment(1L, commentDto);
  		// Checking Result
  		assertEquals("Test Body", commentResponse.getBody());
	}

	@Test
	void testGetCommentsByPostId() {
		// retrieve comments by postId
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);
        // Mock the Actual Database call
  		when(commentRepository.findByPostId(Mockito.anyLong())).thenReturn(comments);
  		// Mock the Actual Mapper call (mapToEntity)
		when(mapper.map(comment, CommentDto.class)).thenReturn(commentDto);
        List<CommentDto> commentDtos = commentService.getCommentsByPostId(Mockito.anyLong());		
        assertNotNull(commentDtos);
	}

	@Test
	void testGetCommentById() {
		// Mock the Actual Database call (findById)
		when(postRepository.findById(Mockito.anyLong())).thenReturn(postOptional);
		when(commentRepository.findById(Mockito.anyLong())).thenReturn(commentOptional);
		// Mock the Actual Mapper call (mapToEntity)
		when(mapper.map(comment, CommentDto.class)).thenReturn(commentDto);
		assertNotNull(commentService.getCommentById(1L, 1L));
	}

	@Test
	void testUpdateComment() {
		// Mock the Actual Database call (findById)
		when(postRepository.findById(Mockito.anyLong())).thenReturn(postOptional);
		when(commentRepository.findById(Mockito.anyLong())).thenReturn(commentOptional);
  		// Mock the Actual Database call
  		when(commentRepository.save(comment)).thenReturn(comment);
		// Mock the Actual Mapper call (mapToDTO)
		when(mapper.map(comment, CommentDto.class)).thenReturn(commentDto);
		assertNotNull(commentService.updateComment(1L, 1L, commentDto));
	}

	@Test
	void testDeleteComment() {
		// Mock the Actual Database call (findById)
		when(postRepository.findById(Mockito.anyLong())).thenReturn(postOptional);
		when(commentRepository.findById(Mockito.anyLong())).thenReturn(commentOptional);
		// Making Original Method Call
		commentService.deleteComment(1L, 1L);
		// Checking Result
		Mockito.verify(commentRepository, Mockito.times(1)).delete(comment);
	}
}