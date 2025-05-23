package org.zerock.apiserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.apiserver.domain.Todo;
import org.zerock.apiserver.dto.PageRequestDto;
import org.zerock.apiserver.dto.PageResponseDto;
import org.zerock.apiserver.dto.TodoDto;
import org.zerock.apiserver.repository.TodoRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final ModelMapper modelMapper;
    private final TodoRepository todoRepository;

    @Override
    public Long register(TodoDto todoDto) {
        log.info(".........");
        Todo todo = modelMapper.map(todoDto, Todo.class);
        Todo saveTodo = todoRepository.save(todo);
        return saveTodo.getTno();
    }

    public TodoDto get(Long tno) {
        Optional<Todo> result = todoRepository.findById(tno);
        Todo todo = result.orElseThrow();
        TodoDto dto = modelMapper.map(todo, TodoDto.class);
        return dto;
    }

    @Override
    public void modify(TodoDto todoDto) {
        Optional<Todo> result = todoRepository.findById(todoDto.getTno());

        Todo todo = result.orElseThrow();
        todo.changeTitle(todoDto.getTitle());
        todo.changeDueDate(todoDto.getDueDate());
        todo.changeComplete(todoDto.isComplete());

        todoRepository.save(todo);
    }

    @Override
    public void remove(Long tno) {
        todoRepository.deleteById(tno);
    }

    @Override
    public PageResponseDto<TodoDto> list(PageRequestDto pageRequestDto) {

        Pageable pageable = PageRequest.of(
                pageRequestDto.getPage() - 1,
                pageRequestDto.getSize(),
                Sort.by("tno").descending()
        );
        Page<Todo> result = todoRepository.findAll(pageable);

        List<TodoDto> dtoList = result.getContent().stream()
                .map(todo -> modelMapper.map(todo, TodoDto.class)).collect(Collectors.toList());

        long totalCount = result.getTotalElements();

        PageResponseDto<TodoDto> responseDto = PageResponseDto.<TodoDto>withAll()
                .dtoList(dtoList)
                .pageRequestDto(pageRequestDto)
                .totalCount(totalCount)
                .build();

        return responseDto;
    }
}
