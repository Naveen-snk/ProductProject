package com.productRetailApp.product.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.productRetailApp.product.entity.ApprovalQueue;
import com.productRetailApp.product.entity.Product;
import com.productRetailApp.product.repository.ApprovalQueueRepository;
import com.productRetailApp.product.repository.ProductRepository;

@Service
public class ApprovalQueueServiceimpl implements ApprovalQueueService {

	private ProductRepository productRepository;

	private ApprovalQueueRepository approvalQueueRepository;

	@Autowired
	public ApprovalQueueServiceimpl(ApprovalQueueRepository productApprovalRespository,
			ProductRepository productRepository) {
		this.approvalQueueRepository = productApprovalRespository;
		this.productRepository = productRepository;
	}

	/*
	 * To Get all the products in the approval queue, sorted by request date
	 * earliest first.
	 */
	@Override
	public List<ApprovalQueue> getAllApprovalQueueProducts() {

		List<ApprovalQueue> approvalList = approvalQueueRepository.findAll();

		List<ApprovalQueue> sortedList = approvalList.stream()
				.sorted(Comparator.comparing(ApprovalQueue::getApprovalDate).reversed()).toList();

		return sortedList;
	}
	
	//To find ApprovalQueue products by the approvalId
	@Override
	public Optional<ApprovalQueue> findProductApprovalbyId(int id) {

		return approvalQueueRepository.findById(id);
	}

	//To Delete ApprovalQueue products by approvalId
	@Override
	public void deleteProductApproval(int id) {

		approvalQueueRepository.deleteById(id);
	}
	
	/*
	 * To approve a product from the approval queue. The product state is
	 * updated, and it is removed from the approval queue.
	 */
	
	@Override
	public void updateInProductAfterApprove(int id) {

		Optional<ApprovalQueue> approvedProduct = approvalQueueRepository.findById(id);

		Product theProduct = new Product();

		theProduct.setProductName(approvedProduct.get().getProductName());
		theProduct.setPrice(approvedProduct.get().getPrice());
		theProduct.setPostedDate(approvedProduct.get().getApprovalDate());
		theProduct.setStatus("APPROVED");

		productRepository.save(theProduct);
	}

}
