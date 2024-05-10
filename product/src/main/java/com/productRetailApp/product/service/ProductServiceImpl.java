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

	@Override
	public Optional<Product> findProductById(int id) {
		return productRepository.findById(id);
	}

	@Override
	public List<Product> getActiveProducts() {
		return productRepository.getActiveProducts();
	}

	@Override
	public List<Product> getProductBySearch(String productName, double minPrice, double maxPrice, Date minPostedDate,
			Date maxPostedDate) {

		List<Product> productList = productRepository.findAll();

		List<Product> productListFilterByPrice = productList.stream().filter(n -> n.getProductName() == productName)
				.toList();

		List<Product> productListFilterByDate = productListFilterByPrice.stream()
				.filter(n -> (n.getPrice() >= minPrice || n.getPrice() <= maxPrice)).toList();

		List<Product> productListFinal = productListFilterByDate.stream()
				.filter(n -> (n.getPostedDate().after(minPostedDate) || n.getPostedDate().before(maxPostedDate)))
				.toList();

		return productListFinal;
	}

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

	@Override
	public Product updateProduct(double price, Product product) {

		price = price + price / 2;

		if (product.getPrice() > price) {
			ApprovalQueue productApproval = new ApprovalQueue();
			productApproval.setProductName(product.getProductName());
			productApproval.setPrice(product.getPrice());
			productApproval.setPostedDate(java.time.LocalDateTime.now());
			productApproval.setApprovalDate(product.getPostedDate());
			productApproval.setStatus("WAITING");
			approvalQueueRepository.save(productApproval);
			return product;
		} else {
			return productRepository.save(product);
		}

	}

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
