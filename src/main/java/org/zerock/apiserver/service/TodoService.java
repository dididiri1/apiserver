package org.zerock.apiserver.service;

import org.zerock.apiserver.dto.PageRequestDto;
import org.zerock.apiserver.dto.PageResponseDto;
import org.zerock.apiserver.dto.TodoDto;

public interface TodoService {

    Long register(TodoDto todoDTO);

    TodoDto get(Long tno);

    void modify(TodoDto todoDto);

    void remove(Long tno);

    PageResponseDto<TodoDto> list(PageRequestDto pageRequestDto);
}
