package com.productRetailApp.product.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.productRetailApp.product.entity.Product;

public interface ProductService {

	public List<Product> getActiveProducts();

	public Optional<Product> findProductById(int id);

	public List<Product> getProductBySearch(String productName, double minPrice, double maxPrice, Date minPostedDate,
			Date maxPostedDate);

	public Product saveProduct(Product product);

	public Product updateProduct(double price, Product product);

	public void deleteProduct(int id);

}
