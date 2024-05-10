package com.productRetailApp.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.productRetailApp.product.entity.ApprovalQueue;

@Repository
public interface ApprovalQueueRepository extends JpaRepository<ApprovalQueue, Integer> {

}
