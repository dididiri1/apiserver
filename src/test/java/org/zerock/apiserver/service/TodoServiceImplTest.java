package org.zerock.apiserver.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.apiserver.dto.PageRequestDto;
import org.zerock.apiserver.dto.PageResponseDto;
import org.zerock.apiserver.dto.TodoDto;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TodoServiceImplTest {

    @Autowired
    private TodoService todoService;

    @Test
    public void testRegister() {
        TodoDto todoTdo = TodoDto.builder().
                title("서비스 테스트")
                .writer("tester")
                .dueDate(LocalDate.of(2025,05,20)).build();

        Long tno = todoService.register(todoTdo);
        assertThat(tno).isNotNull();
    }

    @Test
    public void testGet() {
        Long tno = 1L;
        TodoDto todoDto = todoService.get(tno);
    }

    @Test
    public void testList() {
        PageRequestDto pageRequestDto = PageRequestDto.builder()
                .page(2).size(10).build();

        PageResponseDto<TodoDto> response = todoService.list(pageRequestDto);

        assertThat(response).isNotNull();
    }
}