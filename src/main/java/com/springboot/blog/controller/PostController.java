package com.springboot.blog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.service.PostService;
import com.springboot.blog.utils.AppConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    // create blog post rest api
    @PostMapping
    @Operation(summary = "Adding Post", 
    		   parameters = {@Parameter(name = "Authorization", ref = "Authorization token",  required = true)},
    		   requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = PostDto.class))),
    		   responses = {@ApiResponse(responseCode = "201", description = "Post Added Successfully", content = @Content(schema = @Schema(implementation = String.class))),
    				   		@ApiResponse(responseCode = "400", description = "Invalid Request", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto){
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }

    // get all posts rest api
    @GetMapping
    @Operation(summary = "Fetch All Posts", 
    		   responses = {@ApiResponse(responseCode = "200", description = "Post List", content = @Content(schema = @Schema(implementation = PostResponse.class)))})
    public PostResponse getAllPosts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        return postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
    }

    // get post by id
    @GetMapping("/{id}")
    @Operation(summary = "Fetch Post By Id", 
	   responses = {@ApiResponse(responseCode = "200", description = "Posts By ID", content = @Content(schema = @Schema(implementation = PostDto.class)))})
    public ResponseEntity<PostDto> getPostById(@PathVariable(name = "id") long id){
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    // update post by id rest api
    @PutMapping("/{id}")
    @Operation(summary = "Updating Post", 
	   parameters = {@Parameter(name = "Authorization", ref = "Authorization token",  required = true)},
	   requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = PostDto.class))),
	   responses = {@ApiResponse(responseCode = "201", description = "Post Updated Successfully", content = @Content(schema = @Schema(implementation = String.class))),
			   		@ApiResponse(responseCode = "400", description = "Invalid Request", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto postDto, @PathVariable(name = "id") long id){

       PostDto postResponse = postService.updatePost(postDto, id);

       return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    // delete post rest api
    @DeleteMapping("/{id}")
    @Operation(summary = "Deleting Post", 
	   parameters = {@Parameter(name = "Authorization", ref = "Authorization token",  required = true)},
	   responses = {@ApiResponse(responseCode = "200", description = "Post Deleted Successfully", content = @Content(schema = @Schema(implementation = String.class))),
			   		@ApiResponse(responseCode = "404", description = "Post Not Found", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<String> deletePost(@PathVariable(name = "id") long id){

        postService.deletePostById(id);

        return new ResponseEntity<>("Post entity deleted successfully.", HttpStatus.OK);
    }
}
