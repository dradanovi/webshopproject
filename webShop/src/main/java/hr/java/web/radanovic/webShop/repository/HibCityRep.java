package hr.java.web.radanovic.webShop.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import hr.java.web.radanovic.webShop.model.City;

@Repository
@Transactional
public class HibCityRep implements RepCity {

	@PersistenceContext
	private EntityManager em;

	@Override
	public <S extends City> S save(S entity) {
		em.persist(entity);
		return entity;
	}

	@Override
	public <S extends City> Iterable<S> saveAll(Iterable<S> entities) {
		entities.forEach(e -> em.persist(e));
		return entities;
	}

	@Override
	public Optional<City> findById(Long id) {
		return Optional.ofNullable(em.find(City.class, id));
	}

	@Override
	public boolean existsById(Long id) {
		if (em.find(City.class, id) != null) {
			return true;
		}
		return false;
	}

	@Override
	public Iterable<City> findAll() {
		return em.createQuery("from City", City.class).getResultList();
	}

	@Override
	public Iterable<City> findAllById(Iterable<Long> ids) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<City> cq = cb.createQuery(City.class);
		Root<City> root = cq.from(City.class);
		List<Predicate> predicates = new ArrayList<>();
		ids.forEach(e -> predicates.add(cb.equal(root.get("id"), e)));
		cq.where(cb.or(predicates.toArray(new Predicate[] {})));

		return em.createQuery(cq).getResultList();
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void deleteById(Long id) {
		em.remove(findById(id));
	}

	@Override
	public void delete(City entity) {
		em.remove(entity);
	}

	@Override
	public void deleteAll(Iterable<? extends City> entities) {
		entities.forEach(e -> em.remove(e));
	}

	@Override
	public void deleteAll() {
		findAll().forEach(e -> em.remove(e));
	}

	@Override
	public List<City> findByName(String name) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<City> cq = cb.createQuery(City.class);
		Root<City> root = cq.from(City.class);
		cq.where(cb.equal(root.get("name"), name));
		return em.createQuery(cq).getResultList();
	}

}
