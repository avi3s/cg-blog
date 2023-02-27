package com.springboot.blog.controller;

import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.service.PostService;
import com.springboot.blog.service.impl.PostServiceImpl;
import com.springboot.blog.utils.AppConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class PostControllerTest {

    @Mock
    private PostServiceImpl postService;

    @InjectMocks
    private PostController postController;

    @Test
    public void createPost_shouldReturnCreatedStatus() {
        PostDto postDto = new PostDto();
        postDto.setTitle("Test Title");
        postDto.setDescription("chat decripbtion");
        postDto.setContent("Test Content");

        when(postService.createPost(postDto)).thenReturn(postDto);

        ResponseEntity<PostDto> response = postController.createPost(postDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(postDto);
        verify(postService, times(1)).createPost(postDto);
    }

    @Test
    public void getAllPosts_shouldReturnPostResponse() {
        PostResponse postResponse = new PostResponse();
        when(postService.getAllPosts(
                Integer.parseInt(AppConstants.DEFAULT_PAGE_NUMBER),
                Integer.parseInt(AppConstants.DEFAULT_PAGE_SIZE),
                AppConstants.DEFAULT_SORT_BY,
                AppConstants.DEFAULT_SORT_DIRECTION)
        ).thenReturn(postResponse);

        PostResponse response = postController.getAllPosts(
                Integer.parseInt(AppConstants.DEFAULT_PAGE_NUMBER),
                Integer.parseInt(AppConstants.DEFAULT_PAGE_SIZE),
                AppConstants.DEFAULT_SORT_BY,
                AppConstants.DEFAULT_SORT_DIRECTION
        );

        assertThat(response).isEqualTo(postResponse);
        verify(postService, times(1)).getAllPosts(
                Integer.parseInt(AppConstants.DEFAULT_PAGE_NUMBER),
                Integer.parseInt(AppConstants.DEFAULT_PAGE_SIZE),
                AppConstants.DEFAULT_SORT_BY,
                AppConstants.DEFAULT_SORT_DIRECTION
        );
    }

    @Test
    public void getPostById_shouldReturnPostDto() {
        PostDto postDto = new PostDto();
        postDto.setId(1L);
        when(postService.getPostById(postDto.getId())).thenReturn(postDto);

        ResponseEntity<PostDto> response = postController.getPostById(postDto.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(postDto);
        verify(postService, times(1)).getPostById(postDto.getId());
    }

    @Test
    public void updatePost_shouldReturnUpdatedPostDto() {
        PostDto postDto = new PostDto();
        postDto.setId(1L);
        postDto.setTitle("Updated Title");
        postDto.setContent("Updated Content");
        when(postService.updatePost(postDto, postDto.getId())).thenReturn(postDto);

        ResponseEntity<PostDto> response = postController.updatePost(postDto, postDto.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(postDto);
        verify(postService, times(1)).updatePost(postDto, postDto.getId());
    }

    @Test
    public void deletePost_shouldReturnOkStatus() {
        long id = 1L;
        doNothing().when(postService).deletePostById(id);

        ResponseEntity<String> response = postController.deletePost(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Post entity deleted successfully.");
        verify(postService, times(1)).deletePostById(id);
    }
}
