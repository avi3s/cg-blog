package com.springboot.blog.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.service.impl.CommentServiceImpl;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest {

    @Mock
    private CommentServiceImpl commentService;

    private CommentController commentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        commentController = new CommentController(commentService);
    }

    @Test
    void testCreateComment() {
        CommentDto commentDto = new CommentDto();
        commentDto.setName("chat commentdto test");
        commentDto.setEmail("test22.2@gmail.com");
        commentDto.setBody("chat body test");
        ResponseEntity<CommentDto> expectedResponse = new ResponseEntity<>(commentDto, HttpStatus.CREATED);
        when(commentService.createComment(1L, commentDto)).thenReturn(commentDto);

        ResponseEntity<CommentDto> actualResponse = commentController.createComment(1L, commentDto);

        assertEquals(expectedResponse, actualResponse);
        verify(commentService, times(1)).createComment(1L, commentDto);
    }

    @Test
    void testGetCommentsByPostId() {
        List<CommentDto> commentDtoList = new ArrayList<>();
        when(commentService.getCommentsByPostId(1L)).thenReturn(commentDtoList);

        List<CommentDto> actualCommentDtoList = commentController.getCommentsByPostId(1L);

        assertEquals(commentDtoList, actualCommentDtoList);
        verify(commentService, times(1)).getCommentsByPostId(1L);
    }

    @Test
    void testGetCommentById() {
        CommentDto commentDto = new CommentDto();
        ResponseEntity<CommentDto> expectedResponse = new ResponseEntity<>(commentDto, HttpStatus.OK);
        when(commentService.getCommentById(1L, 1L)).thenReturn(commentDto);

        ResponseEntity<CommentDto> actualResponse = commentController.getCommentById(1L, 1L);

        assertEquals(expectedResponse, actualResponse);
        verify(commentService, times(1)).getCommentById(1L, 1L);
    }

    @Test
    void testUpdateComment() {
        CommentDto commentDto = new CommentDto();
        ResponseEntity<CommentDto> expectedResponse = new ResponseEntity<>(commentDto, HttpStatus.OK);
        when(commentService.updateComment(1L, 1L, commentDto)).thenReturn(commentDto);

        ResponseEntity<CommentDto> actualResponse = commentController.updateComment(1L, 1L, commentDto);

        assertEquals(expectedResponse, actualResponse);
        verify(commentService, times(1)).updateComment(1L, 1L, commentDto);
    }

    @Test
    void testDeleteComment() {
        ResponseEntity<String> expectedResponse = new ResponseEntity<>("Comment deleted successfully", HttpStatus.OK);

        ResponseEntity<String> actualResponse = commentController.deleteComment(1L, 1L);

        assertEquals(expectedResponse, actualResponse);
        verify(commentService, times(1)).deleteComment(1L, 1L);
    }
}
