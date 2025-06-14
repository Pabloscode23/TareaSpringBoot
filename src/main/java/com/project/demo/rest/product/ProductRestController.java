package com.project.demo.rest.product;
import com.project.demo.logic.entity.product.Product;
import com.project.demo.logic.entity.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductRestController {
    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN_ROLE')")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(product.getName());
                    existingProduct.setDescription(product.getDescription());
                    existingProduct.setPrice(product.getPrice());
                    existingProduct.setStockQuantity(product.getStockQuantity());
                    existingProduct.setCategory(product.getCategory());
                    return productRepository.save(existingProduct);
                })
                .orElseGet(() -> {
                    product.setId(id);
                    return productRepository.save(product);
                });
    }
    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN_ROLE')")
    public Product createProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN_ROLE')")
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
    }
}
