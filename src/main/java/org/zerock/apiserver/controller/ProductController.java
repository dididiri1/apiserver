package org.zerock.apiserver.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.apiserver.dto.PageRequestDto;
import org.zerock.apiserver.dto.PageResponseDto;
import org.zerock.apiserver.dto.ProductDto;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.zerock.apiserver.service.ProductService;
import org.zerock.apiserver.util.CustomFileUtil;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/products")
public class ProductController {


    private final CustomFileUtil fileUtil;

    private final ProductService productService;

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @GetMapping("/list")
    public PageResponseDto<ProductDto> list(PageRequestDto pageRequestDto) {
        log.info("list....." + pageRequestDto);

        return productService.getList(pageRequestDto);
    }

    @PostMapping("/")
    public Map<String, Long> register(ProductDto productDto) {

        log.info("rgister: " + productDto);

        List<MultipartFile> files = productDto.getFiles();

        List<String> uploadFileNames = fileUtil.saveFiles(files);

        productDto.setUploadFileNames(uploadFileNames);

        log.info(uploadFileNames);

        Long pno = productService.register(productDto);

        return Map.of("result", pno);
    }

    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable String fileName) {

        return fileUtil.getFile(fileName);
    }

    @GetMapping("/{tno}")
    public ProductDto read(@PathVariable(name = "tno") Long tno) {
        return productService.get(tno);
    }

    @PutMapping("/{pno}")
    public Map<String, String> modify(@PathVariable(name = "pno") Long pno, ProductDto productDto) {
        productDto.setPno(pno);
        ProductDto oldProductDTO = productService.get(pno);
        //기존의 파일들 (데이터베이스에 존재하는 파일들 - 수정 과정에서 삭제되었을 수 있음)
        List<String> oldFileNames = oldProductDTO.getUploadFileNames();
        //새로 업로드 해야 하는 파일들
        List<MultipartFile> files = productDto.getFiles();
        //새로 업로드되어서 만들어진 파일 이름들
        List<String> currentUploadFileNames = fileUtil.saveFiles(files);
        //화면에서 변화 없이 계속 유지된 파일들
        List<String> uploadedFileNames = productDto.getUploadFileNames();
        //유지되는 파일들 + 새로 업로드된 파일 이름들이 저장해야 하는 파일 목록이 됨
        if (currentUploadFileNames != null && currentUploadFileNames.size() > 0) {
            uploadedFileNames.addAll(currentUploadFileNames);
        }

        //수정 작업
        productService.modify(productDto);
        if (oldFileNames != null && oldFileNames.size() > 0) {
            //지워야 하는 파일 목록 찾기
            //예전 파일들 중에서 지워져야 하는 파일이름들
            List<String> removeFiles = oldFileNames.stream().filter(fileName -> uploadedFileNames.indexOf(fileName) == -1).collect(Collectors.toList());
            //실제 파일 삭제
            fileUtil.deleteFiles(removeFiles);
        }

        return Map.of("RESULT", "SUCCESS");
    }

    @DeleteMapping("/{pno}")
    public Map<String, String> remove(@PathVariable("pno") Long pno) {
        List<String> oldFileNames = productService.get(pno).getUploadFileNames();
        productService.remove(pno);
        fileUtil.deleteFiles(oldFileNames);

        return Map.of("RESULT", "SUCCESS");

    }

}
