package com.iko.restapi.service.product;

import com.iko.restapi.domain.product.Product;
import com.iko.restapi.repository.product.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductJpaRepository productRepository;

    public List<Product> products(int page, int size, String sortBy) {
        if (sortBy.startsWith("date")) {
            if (sortBy.endsWith("asc")) {
                return productRepository
                        .findAll(PageRequest.of(page, size, Sort.by("createdAt").ascending()))
                        .toList();
            } else {
                return productRepository.
                        findAll(PageRequest.of(page, size, Sort.by("createdAt").descending()))
                        .toList();
            }
        } else if (sortBy.startsWith("price")) {
            if (sortBy.endsWith("asc")) {
                return productRepository
                        .findAll(PageRequest.of(page, size, Sort.by("sellPrice").ascending()))
                        .toList();
            } else {
                return productRepository.
                        findAll(PageRequest.of(page, size, Sort.by("sellPrice").descending()))
                        .toList();
            }
        }
        // TODO: 통계를 기반한 추천순 쿼리
        return productRepository.findAll(PageRequest.of(page, size)).toList();
    }
}
