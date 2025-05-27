package org.zerock.apiserver.service;

import org.zerock.apiserver.dto.PageRequestDto;
import org.zerock.apiserver.dto.PageResponseDto;
import org.zerock.apiserver.dto.ProductDto;

public interface ProductService {

    public PageResponseDto<ProductDto> getList(PageRequestDto pageRequestDto);
}
