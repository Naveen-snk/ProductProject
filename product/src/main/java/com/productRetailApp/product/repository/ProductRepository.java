package com.productRetailApp.product.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.productRetailApp.product.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

	@Query("from Product Where status = 'APPROVED' order by postedDate DESC")
	public List<Product> getActiveProducts();

	@Query(value = "select * from product Where product_name = :productName" + " UNION"
			+ " select * from product where price BETWEEN :minPrice AND :maxPrice" + " UNION"
			+ " select * from product where posted_date BETWEEN :minPostedDate AND :maxPostedDate", nativeQuery = true)
	public List<Product> getProductsBySearch(String productName, double minPrice, double maxPrice, Date minPostedDate,
			Date maxPostedDate);

}
