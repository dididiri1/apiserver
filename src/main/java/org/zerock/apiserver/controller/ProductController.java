package org.zerock.apiserver.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.apiserver.dto.ProductDto;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.zerock.apiserver.util.CustomFileUtil;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/products")
public class ProductController {


  private final CustomFileUtil fileUtil;

  @PostMapping("/")
  public Map<String, Long> register(ProductDto productDto) {

    log.info("rgister: " + productDto);

    List<MultipartFile> files = productDto.getFiles();

    List<String> uploadFileNames = fileUtil.saveFiles(files);

    productDto.setUploadFileNames(uploadFileNames);

    log.info(uploadFileNames);

    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    //서비스 호출
    //Long pno = productService.register(productDTO);

    return Map.of("result", 1L);
  }

  @GetMapping("/view/{fileName}")
  public ResponseEntity<Resource> viewFileGET(@PathVariable String fileName) {
    return fileUtil.getFile(fileName);
  }
}
