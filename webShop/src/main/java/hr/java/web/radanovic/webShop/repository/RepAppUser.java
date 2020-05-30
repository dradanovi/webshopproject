package hr.java.web.radanovic.webShop.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import hr.java.web.radanovic.webShop.model.AppUser;

public interface RepAppUser extends CrudRepository<AppUser, Long>{	
	
	Optional<AppUser> findByUsername(String username);

}
