package org.zerock.apiserver.contoller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import org.zerock.apiserver.dto.PageRequestDto;
import org.zerock.apiserver.dto.PageResponseDto;
import org.zerock.apiserver.dto.TodoDto;
import org.zerock.apiserver.service.TodoService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/todo")
public class TodoController {

    private final TodoService todoService;

    @GetMapping("/{tno}")
    public TodoDto get(@PathVariable(name = "tno") Long tno) {
        return todoService.get(tno);
    }

    @GetMapping("/list")
    public PageResponseDto<TodoDto> list(PageRequestDto pageRequestDto) {
        return todoService.list(pageRequestDto);
    }

    @PostMapping("/")
    public Map<String, Long> register(@RequestBody TodoDto todoDto){
        log.info("TodoDTO: " + todoDto);
        Long tno = todoService.register(todoDto);
        return Map.of("TNO", tno);
    }

    @PutMapping("/{tno}")
    public Map<String, String> modify(@PathVariable(name="tno") Long tno, @RequestBody TodoDto todoDto) {
        todoDto.setTno(tno);
        log.info("Modify: " + todoDto);
        todoService.modify(todoDto);
        return Map.of("RESULT", "SUCCESS");
    }

    @DeleteMapping("/{tno}")
    public Map<String, String> remove( @PathVariable(name="tno") Long tno ){
        log.info("Remove: " + tno);
        todoService.remove(tno);
        return Map.of("RESULT", "SUCCESS");
    }
}
