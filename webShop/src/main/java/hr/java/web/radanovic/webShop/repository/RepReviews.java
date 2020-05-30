package hr.java.web.radanovic.webShop.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import hr.java.web.radanovic.webShop.model.AppUser;
import hr.java.web.radanovic.webShop.model.Product;
import hr.java.web.radanovic.webShop.model.Review;

public interface RepReviews extends CrudRepository<Review, Long> {

	List<Review> findByUser(AppUser user);
	List<Review> findByProduct(Product product);
}
