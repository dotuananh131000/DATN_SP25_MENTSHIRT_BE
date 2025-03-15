package com.java.project.controllers;

import com.java.project.dtos.ApiResponse;
import com.java.project.dtos.ChatLieuDto;
import com.java.project.models.ChatLieuModel;
import com.java.project.services.ChatLieuService;
import com.java.project.exceptions.EntityAlreadyExistsException;
import com.java.project.exceptions.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat-lieu")
public class ChatLieuController {

    @Autowired
    private ChatLieuService chatLieuService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllChatLieu(
            @RequestParam(value = "search", defaultValue = "") String search,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.by(sort).with(Sort.Direction.fromString(direction))));
            Page<ChatLieuDto> chatLieuDtos = chatLieuService.getAll(search, pageable);
            return ResponseEntity.ok(new ApiResponse("success", "Lấy dữ liệu thành công", chatLieuDtos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("error", "Có lỗi xảy ra trong quá trình xử lý", null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Integer id) {
        try {
            ChatLieuDto chatLieuDto = chatLieuService.getById(id);
            return ResponseEntity.ok(new ApiResponse("success", "Lấy dữ liệu thành công", chatLieuDto));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("error", "Chất liệu không tồn tại", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("error", "Có lỗi xảy ra trong quá trình xử lý", null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> addChatLieu(@Valid @RequestBody ChatLieuModel chatLieuModel, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                String errorMessage = bindingResult.getAllErrors().stream()
                        .map(ObjectError::getDefaultMessage)
                        .collect(Collectors.joining(", "));
                return ResponseEntity.badRequest().body(new ApiResponse("error", "Validation Error: " + errorMessage, null));
            }

            ChatLieuDto newChatLieu = chatLieuService.addChatLieu(chatLieuModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("success", "Chất liệu đã được thêm thành công", newChatLieu));

        } catch (EntityAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("error", "Tên chất liệu đã tồn tại", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", "Có lỗi xảy ra trong quá trình xử lý", null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateChatLieu(@PathVariable Integer id,
                                                      @Valid @RequestBody ChatLieuModel chatLieuModel,
                                                      BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                String errorMessage = bindingResult.getAllErrors().stream()
                        .map(ObjectError::getDefaultMessage)
                        .collect(Collectors.joining(", "));
                return ResponseEntity.badRequest().body(new ApiResponse("error", "Validation Error: " + errorMessage, null));
            }

            ChatLieuDto updatedChatLieu = chatLieuService.updateChatLieu(id, chatLieuModel);
            return ResponseEntity.ok(new ApiResponse("success", "Cập nhật thành công", updatedChatLieu));

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("error", "Chất liệu không tồn tại", null));
        } catch (EntityAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("error", "Tên chất liệu đã tồn tại", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("error", "Có lỗi xảy ra trong quá trình xử lý", null));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse> toggleTrangThai(@PathVariable Integer id) {
        try {
            chatLieuService.toggleTrangThai(id);
            return ResponseEntity.ok(new ApiResponse("success", "Trạng thái đã được cập nhật", null));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("error", "Chất liệu không tồn tại", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("error", "Có lỗi xảy ra trong quá trình xử lý", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteChatLieu(@PathVariable Integer id) {
        try {
            chatLieuService.deleteChatLieu(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("error", "Chất liệu không tồn tại", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("error", "Có lỗi xảy ra trong quá trình xử lý", null));
        }
    }
}

