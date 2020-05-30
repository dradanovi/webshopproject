package hr.java.web.radanovic.webShop.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import hr.java.web.radanovic.webShop.model.AppUser;
import hr.java.web.radanovic.webShop.model.AppUserDetails;

public interface RepAppUserDetails extends CrudRepository<AppUserDetails, Long> {
	
	Optional<AppUserDetails> findByUser(AppUser user);

}
