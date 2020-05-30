package hr.java.web.radanovic.webShop.repository;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import hr.java.web.radanovic.webShop.enums.CountryEnum;
import hr.java.web.radanovic.webShop.model.Country;

@Repository
@Transactional
public class HibCountryRep implements RepCountry {

	@PersistenceContext
	private EntityManager em;
	
	public Optional<Country> findByCountryName(CountryEnum countryName) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Country> cq = cb.createQuery(Country.class);
		Root<Country> root = cq.from(Country.class);
		cq.where(cb.equal(root.get("countryName"), countryName));
		try {
			return Optional.ofNullable(em.createQuery(cq).getSingleResult());
		} catch (NoResultException e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}

}
