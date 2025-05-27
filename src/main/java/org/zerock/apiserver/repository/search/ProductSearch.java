package org.zerock.apiserver.repository.search;

import org.zerock.apiserver.dto.PageRequestDto;
import org.zerock.apiserver.dto.PageResponseDto;
import org.zerock.apiserver.dto.ProductDto;

public interface ProductSearch {

    PageResponseDto<ProductDto> searchList (PageRequestDto pageRequestDto);
}
