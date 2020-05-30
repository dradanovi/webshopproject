package hr.java.web.radanovic.webShop.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import hr.java.web.radanovic.webShop.model.Product;
import hr.java.web.radanovic.webShop.model.Seller;

public interface RepProduct extends CrudRepository<Product, Long> {

	List<Product> findBySeller(Seller seller);
	
}
