package org.zerock.apiserver.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.apiserver.domain.Product;

import java.util.Arrays;
import java.util.Optional;

@SpringBootTest
public class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Test
    public void testInsert() {
        for (int i = 0; i < 10; i++) {
            Product product = Product.builder()
                    .pname("상품" +i)
                    .price(100*i)
                    .pdesc("상품설명 " +i)
                    .build();

            product.addImageString("IMAGE1.jpg");
            product.addImageString("IMAGE2.jpg");
            productRepository.save(product);
        }
    }


    @Test
    public void testRead() {

        for (int i = 0; i < 10; i++) {
            Product product = Product.builder()
                    .pname("상품" +i)
                    .price(100*i)
                    .pdesc("상품설명 " +i)
                    .build();

            product.addImageString("IMAGE1.jpg");
            product.addImageString("IMAGE2.jpg");
            productRepository.save(product);
        }

        Long pno = 1L;

        Optional<Product> result = productRepository.selectOne(pno);

        Product product = result.orElseThrow();

        System.out.println(product);
    }

    @Commit
    @Transactional
    @Test
    public void testDelete() {
        Long pno = 2L;
        productRepository.updateToDelete(pno, true);
    }

    @Test
    public void testList() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("pno").descending());
        Page<Object[]> result = productRepository.selectList(pageable);
        result.getContent().forEach(arr -> System.out.println(Arrays.toString(arr)));
    }
}
