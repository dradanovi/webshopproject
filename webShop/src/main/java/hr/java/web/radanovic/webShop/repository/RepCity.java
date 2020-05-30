package hr.java.web.radanovic.webShop.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import hr.java.web.radanovic.webShop.model.City;

public interface RepCity extends CrudRepository<City, Long>{

	List<City> findByName(String name);
	
}
