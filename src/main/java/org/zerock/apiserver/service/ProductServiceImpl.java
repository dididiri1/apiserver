package org.zerock.apiserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.apiserver.domain.Product;
import org.zerock.apiserver.domain.ProductImage;
import org.zerock.apiserver.dto.PageRequestDto;
import org.zerock.apiserver.dto.PageResponseDto;
import org.zerock.apiserver.dto.ProductDto;
import org.zerock.apiserver.repository.ProductRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public PageResponseDto<ProductDto> getList(PageRequestDto pageRequestDTO) {
        log.info("getList..............");

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(), Sort.by("pno").descending());

        Page<Object[]> result = productRepository.selectList(pageable);

        List<ProductDto> dtoList = result.get().map(arr -> {
            Product product = (Product) arr[0];
            ProductImage productImage = (ProductImage) arr[1];

            ProductDto productDTO = ProductDto.builder().pno(product.getPno()).pname(product.getPname()).pdesc(product.getPdesc()).price(product.getPrice()).build();

            String imageStr = productImage.getFileName();
            productDTO.setUploadFileNames(List.of(imageStr));

            return productDTO;
        }).collect(Collectors.toList());

        long totalCount = result.getTotalElements();

        return PageResponseDto.<ProductDto>withAll().dtoList(dtoList).totalCount(totalCount).pageRequestDto(pageRequestDTO).build();
    }

    @Override
    public Long register(ProductDto productDto) {
        Product product = dtoToEntity(productDto);

        Product result = productRepository.save(product);

        return result.getPno();
    }

    @Override
    public ProductDto get(Long pno) {
        java.util.Optional<Product> result = productRepository.selectOne(pno);
        Product product = result.orElseThrow();
        ProductDto productDTO = entityToDTO(product);
        return productDTO;
    }

    @Override
    public void modify(ProductDto productDto) {
        Optional<Product> result = productRepository.findById(productDto.getPno());

        Product product = result.orElseThrow();

        product.changeName(productDto.getPname());
        product.changeDesc(productDto.getPdesc());
        product.changePrice(productDto.getPrice());

        product.clearList();

        List<String> uploadFileNames = productDto.getUploadFileNames();

        if (uploadFileNames != null && uploadFileNames.size() > 0) {
            uploadFileNames.stream().forEach(uploadName -> {
                product.addImageString(uploadName);
            });
        }
        productRepository.save(product);
    }

    @Override
    public void remove(Long pno) {
        productRepository.updateToDelete(pno, true);
    }

    private ProductDto entityToDTO(Product product) {

        ProductDto productDto = ProductDto.builder().pno(product.getPno()).pname(product.getPname()).pdesc(product.getPdesc()).price(product.getPrice()).delFlag(product.isDelFlag()).build();

        List<ProductImage> imageList = product.getImageList();

        if (imageList == null || imageList.size() == 0) {
            return productDto;
        }

        List<String> fileNameList = imageList.stream().map(productImage -> productImage.getFileName()).toList();

        productDto.setUploadFileNames(fileNameList);

        return productDto;
    }

    private Product dtoToEntity(ProductDto productDto) {
        Product product = Product.builder().pno(productDto.getPno()).pname(productDto.getPname()).pdesc(productDto.getPdesc()).price(productDto.getPrice()).build();

        List<String> uploadFileNames = productDto.getUploadFileNames();
        if (uploadFileNames == null) {
            return product;
        }
        uploadFileNames.stream().forEach(uploadName -> {
            product.addImageString(uploadName);
        });
        return product;
    }
}
