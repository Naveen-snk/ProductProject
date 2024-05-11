package com.productRetailApp.product.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.productRetailApp.product.entity.ApprovalQueue;
import com.productRetailApp.product.entity.Product;
import com.productRetailApp.product.service.ApprovalQueueService;
import com.productRetailApp.product.service.ProductService;

@RestController 
@RequestMapping("/api/products")
public class ProductController {

	private ProductService productService;

	private ApprovalQueueService approvalQueueService;

	@Autowired
	public ProductController(ProductService productService, ApprovalQueueService approvalQueueService) {
		this.productService = productService;
		this.approvalQueueService = approvalQueueService;
	}

	@GetMapping()
	public List<Product> getProducts() {

		if (productService.getActiveProducts().isEmpty())
			throw new RuntimeException("No Active Products available");
		else
			return productService.getActiveProducts();
	}

	@GetMapping("/search")
	public List<Product> getProductBySearch(@RequestParam(required = false) String productName,
			@RequestParam(required = false) double minPrice, @RequestParam(required = false) double maxPrice,
			@RequestParam(required = false) Date minPostedDate, @RequestParam(required = false) Date maxPostedDate) {

		return productService.getProductBySearch(productName, minPrice, maxPrice, minPostedDate, maxPostedDate);
	}

	@PostMapping()
	public ResponseEntity<Product> saveProduct(@RequestBody Product product) {

		if (product.getPrice() > 10000) {
			return new ResponseEntity<Product>(HttpStatus.BAD_REQUEST);
		}
		Product productResult = productService.saveProduct(product);

		if (productResult.getProductId() != 0)
			return new ResponseEntity<Product>(HttpStatus.OK);
		else
			return new ResponseEntity<Product>(HttpStatus.BAD_REQUEST);

	}

	@PutMapping("/{productId}")
	public Product updateProduct(@RequestBody Product product, @PathVariable int productId) {

		Optional<Product> theProduct = productService.findProductById(productId);
		if (theProduct.isPresent()) {
			product.setProductId(productId);
			double thePrice = theProduct.get().getPrice();
			return productService.updateProduct(thePrice, product);
		} else {
			throw new RuntimeException("The Given id is not available or invalid :" + productId);
		}
	}

	@DeleteMapping("/{productId}")
	public ResponseEntity<Product> deleteProduct(@PathVariable int productId) {

		if (productService.findProductById(productId).isPresent() || productId < 0) {
			productService.deleteProduct(productId);
			return new ResponseEntity<Product>(HttpStatus.OK);
		} else {
			throw new RuntimeException("The Product for the Given id is not available :" + productId);
		}

	}

	@GetMapping("/approval-queue")
	public List<ApprovalQueue> getAllApprovalQueueProducts() {
		return approvalQueueService.getAllApprovalQueueProducts();
	}

	@PutMapping("/approval-queue/{approvalId}/approve")
	public ResponseEntity<ApprovalQueue> toApproveProductInQueue(@PathVariable int approvalId) {

		if (approvalQueueService.findProductApprovalbyId(approvalId).isPresent() || approvalId < 0) {

			approvalQueueService.updateInProductAfterApprove(approvalId);

			approvalQueueService.deleteProductApproval(approvalId);

			return new ResponseEntity<ApprovalQueue>(HttpStatus.OK);
		} else {

			throw new RuntimeException(
					"The Product for the Given id is not available in the Approval Queue :" + approvalId);
		}
	}

	@PutMapping("/approval-queue/{approvalId}/reject")
	public ResponseEntity<ApprovalQueue> toRejectProductInQueue(@PathVariable int approvalId) {

		if (approvalQueueService.findProductApprovalbyId(approvalId).isPresent() || approvalId < 0) {

			approvalQueueService.deleteProductApproval(approvalId);

			return new ResponseEntity<ApprovalQueue>(HttpStatus.OK);
		} else {
			throw new RuntimeException(
					"The Product for the Given id is not available in the Approval Queue :" + approvalId);
		}

	}

}
