package org.zerock.apiserver.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.apiserver.domain.Todo;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@SpringBootTest
@Transactional
public class TodoRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;

    @Test
    public void test1() throws Exception {
        //given
        Assertions.assertNotNull(todoRepository);
        //when

        //then
    }

    @Test
    public void testInsert() throws Exception {
        //given
        Todo todo = Todo.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .dueDate(LocalDate.of(2025, 5, 19))
                .build();
        //when
        Todo findTodo = todoRepository.save(todo);

        //then
        assertThat(findTodo.getTitle()).isEqualTo("제목입니다.");
        assertThat(findTodo.getContent()).isEqualTo("내용입니다.");
    }

    @Test
    public void testRead() throws Exception {
        //given
        Todo todo = Todo.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .dueDate(LocalDate.of(2025, 5, 19))
                .build();

        Todo savedTodo = todoRepository.save(todo);

        //when
        Optional<Todo> result = todoRepository.findById(savedTodo.getTno());

        //then
        assertThat(result).isPresent();
        Todo foundTodo = result.get();
        assertThat(foundTodo.getTitle()).isEqualTo("제목입니다.");
        assertThat(foundTodo.getContent()).isEqualTo("내용입니다.");

    }

    @Test
    public void testUpdate() throws Exception {
        //given
        Todo request = Todo.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .dueDate(LocalDate.of(2025, 5, 19))
                .build();

        Todo saveTodo = todoRepository.save(request);

        //when
        saveTodo.changeTitle("제목수정입니다.");
        saveTodo.changeContent("제목내용입니다.");
        todoRepository.save(saveTodo);

        Optional<Todo> result = todoRepository.findById(saveTodo.getTno());
        Todo todo = result.orElseThrow();

        //then
        assertThat(todo.getTitle()).isEqualTo("제목수정입니다.");
        assertThat(todo.getContent()).isEqualTo("제목내용입니다.");

    }
    
    @Test
    public void testPaging() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 10, Sort.by("tno").descending());
        
        //when
        Page<Todo> result = todoRepository.findAll(pageable);
        
        //then
        
        
    }

    @Test
    public void testSearch1() throws Exception {
        //given
        todoRepository.search1();

        //when

        //then


    }
}
