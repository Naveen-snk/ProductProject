package com.productRetailApp.product.entity;

import java.time.LocalDateTime;
import java.util.Date;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "approval_queue")
public class ApprovalQueue {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "approval_id")
	private int approvalId;

	@Column(name = "product_name")
	private String productName;

	@Column(name = "price")
	private double price;
	
	public ApprovalQueue() {}

	public ApprovalQueue(int approvalId, String productName, double price, LocalDateTime postedDate, Date approvalDate,
			String status) {
		super();
		this.approvalId = approvalId;
		this.productName = productName;
		this.price = price;
		this.postedDate = postedDate;
		this.approvalDate = approvalDate;
		this.status = status;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "posted_date")
	private LocalDateTime postedDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "approval_date")
	private Date approvalDate;

	public int getApprovalId() {
		return approvalId;
	}

	public void setApprovalId(int approvalId) {
		this.approvalId = approvalId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public LocalDateTime getPostedDate() {
		return postedDate;
	}

	public void setPostedDate(LocalDateTime localDateTime) {
		this.postedDate = localDateTime;
	}

	public Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "status")
	private String status;

//    @OneToOne(mappedBy = "approvalId" 
//			, cascade = {CascadeType.ALL})
//	//@JsonManagedReference 
//    private Product product;
}
