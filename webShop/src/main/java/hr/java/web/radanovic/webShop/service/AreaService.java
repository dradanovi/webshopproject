package hr.java.web.radanovic.webShop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.java.web.radanovic.webShop.enums.CountryEnum;
import hr.java.web.radanovic.webShop.model.City;
import hr.java.web.radanovic.webShop.model.Country;
import hr.java.web.radanovic.webShop.repository.HibCityRep;
import hr.java.web.radanovic.webShop.repository.HibCountryRep;

@Service
public class AreaService {

	@Autowired
	private HibCityRep cityRepo;

	@Autowired
	private HibCountryRep countryRepo;

	/**
	 * returns the country in the database that is associated with one of the
	 * enumerations in CountryEnum
	 * 
	 * @param countryName
	 * @return
	 */
	public Country getCountry(CountryEnum countryName) {
		return countryRepo.findByCountryName(countryName).get();
	}

	/**
	 * finds a duplicate city in database if it exists and returns it and if it
	 * doesn't exist in the database returns the received City object
	 * 
	 * @param city
	 * @return
	 */
	public City findDuplicate(City city) {
		List<City> list = cityRepo.findByName(city.getName().toLowerCase());
		return (list.contains(city)) ? list.get(list.indexOf(city)) : city;
	}

}
