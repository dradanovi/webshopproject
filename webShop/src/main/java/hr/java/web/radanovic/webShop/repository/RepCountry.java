package hr.java.web.radanovic.webShop.repository;

import java.util.Optional;

import hr.java.web.radanovic.webShop.enums.CountryEnum;
import hr.java.web.radanovic.webShop.model.Country;

public interface RepCountry {

	Optional<Country> findByCountryName(CountryEnum countryName);
	
}
