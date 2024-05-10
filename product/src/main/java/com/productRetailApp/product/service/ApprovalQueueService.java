package com.productRetailApp.product.service;

import java.util.List;
import java.util.Optional;

import com.productRetailApp.product.entity.ApprovalQueue;

public interface ApprovalQueueService {

	public List<ApprovalQueue> getAllApprovalQueueProducts();

	public Optional<ApprovalQueue> findProductApprovalbyId(int id);

	public void deleteProductApproval(int id);

	public void updateInProductAfterApprove(int id);

}
