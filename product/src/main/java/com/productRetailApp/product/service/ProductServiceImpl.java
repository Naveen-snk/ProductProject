package com.productRetailApp.product.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.productRetailApp.product.entity.ApprovalQueue;
import com.productRetailApp.product.entity.Product;
import com.productRetailApp.product.repository.ApprovalQueueRepository;
import com.productRetailApp.product.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

	private ProductRepository productRepository;

	private ApprovalQueueRepository approvalQueueRepository;

	@Autowired
	public ProductServiceImpl(ProductRepository productRepository, ApprovalQueueRepository approvalQueueRepository) {
		this.productRepository = productRepository;
		this.approvalQueueRepository = approvalQueueRepository;
	}

	// To Find product By Id
	@Override
	public Optional<Product> findProductById(int id) {
		return productRepository.findById(id);
	}

	// To Get all the products with Status as ACTIVE(APPROVED)
	@Override
	public List<Product> getActiveProducts() {
		return productRepository.getActiveProducts();
	}

	// To Get Product List based on the given search parameters
	@Override
	public List<Product> getProductBySearch(String productName, double minPrice, double maxPrice, Date minPostedDate,
			Date maxPostedDate) {

		return productRepository.getProductsBySearch(productName, minPrice, maxPrice, minPostedDate, maxPostedDate);
	}

	/*
	 * To Save Product If the price more than $10,000 - product not saved //If the
	 * price is more than $5,000, the product pushed to the approval queue.
	 */
	@Override
	public Product saveProduct(Product product) {

		if (product.getPrice() > 5000) {
			ApprovalQueue productApproval = new ApprovalQueue();
			productApproval.setProductName(product.getProductName());
			productApproval.setPrice(product.getPrice());
			productApproval.setPostedDate(java.time.LocalDateTime.now());
			productApproval.setApprovalDate(product.getPostedDate());
			productApproval.setStatus("WAITING");

			approvalQueueRepository.save(productApproval);
			return product;
		} else
			return productRepository.save(product);
	}

	/*
	 * Updating an existing product, if the price is more than 50% of its previous
	 * price, the product is pushed to the approval queue.
	 */
	@Override
	public Product updateProduct(double price, Product product) {

		price = price + (price / 2);

		if (product.getPrice() > price) {
			ApprovalQueue productApproval = new ApprovalQueue();
			productApproval.setProductName(product.getProductName());
			productApproval.setPrice(product.getPrice());
			productApproval.setPostedDate(java.time.LocalDateTime.now());
			productApproval.setApprovalDate(product.getPostedDate());
			productApproval.setStatus("WAITING");
			approvalQueueRepository.save(productApproval);
			product.setStatus("WAITING");
			return product;
		} else {
			return productRepository.save(product);
		}

	}

	// Delete a product, and the product is moved to the approval queue
	@Override
	public void deleteProduct(int id) {
		Optional<Product> product = productRepository.findById(id);
		ApprovalQueue productApproval = new ApprovalQueue();
		productApproval.setProductName(product.get().getProductName());
		productApproval.setPrice(product.get().getPrice());
		productApproval.setPostedDate(java.time.LocalDateTime.now());
		productApproval.setApprovalDate(product.get().getPostedDate());
		productApproval.setStatus("WAITING");
		approvalQueueRepository.save(productApproval);

		productRepository.deleteById(id);
	}

}
