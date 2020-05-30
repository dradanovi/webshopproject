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

import hr.java.web.radanovic.webShop.model.Product;
import hr.java.web.radanovic.webShop.model.Seller;

@Repository
@Transactional
public class HibProductRep implements RepProduct {

	@PersistenceContext
	private EntityManager em;

	@Override
	public <S extends Product> S save(S entity) {
		em.persist(entity);
		return entity;
	}

	@Override
	public <S extends Product> Iterable<S> saveAll(Iterable<S> entities) {
		entities.forEach(e -> em.persist(e));
		return entities;
	}

	@Override
	public Optional<Product> findById(Long id) {
		return Optional.ofNullable(em.find(Product.class, id));
	}

	@Override
	public boolean existsById(Long id) {
		if (em.find(Product.class, id) != null) {
			return true;
		}
		return false;
	}

	@Override
	public Iterable<Product> findAll() {
		return em.createQuery("from Product", Product.class).getResultList();
	}

	public Iterable<Product> findAllAvailable() {
		return queryBuilder(null, true, true, null);
	}

	@Override
	public Iterable<Product> findAllById(Iterable<Long> ids) {
		return queryBuilder(null, false, false, ids);
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
	public void delete(Product entity) {
		em.remove(entity);
	}

	@Override
	public void deleteAll(Iterable<? extends Product> entities) {
		entities.forEach(e -> em.remove(e));
	}

	@Override
	public void deleteAll() {
		findAll().forEach(e -> em.remove(e));
	}

	@Override
	public List<Product> findBySeller(Seller seller) {
		return queryBuilder(seller, true, false, null);
	}

	public List<Product> findAvailableBySeller(Seller seller) {
		return queryBuilder(seller, true, true, null);
	}

	public List<Product> findUnfinishedBySeller(Seller seller) {
		return queryBuilder(seller, false, true, null);
	}

	public List<Product> findByFilter(Long costFrom, Long costTo, String name) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Product> cq = cb.createQuery(Product.class);
		Root<Product> root = cq.from(Product.class);
		List<Predicate> predicates = new ArrayList<>();
		if (costFrom != null) {
			predicates.add(cb.greaterThanOrEqualTo(root.get("cost"), costFrom));
		}
		if (costTo != null) {
			predicates.add(cb.lessThanOrEqualTo(root.get("cost"), costTo));
		}
		if (name.length() != 0) {
			predicates.add(cb.equal(root.get("name"), name));
		}
		predicates.add(cb.greaterThan(root.get("numberAvailable"), 0));
		predicates.add(cb.equal(root.get("enabled"), true));
		cq.where(cb.and(predicates.toArray(new Predicate[] {})));
		return em.createQuery(cq).getResultList();
	}

	private List<Product> queryBuilder(Seller seller, boolean enabled, boolean available, Iterable<Long> ids) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Product> cq = cb.createQuery(Product.class);
		Root<Product> root = cq.from(Product.class);
		List<Predicate> predicates = new ArrayList<>();
		if (seller != null) {
			predicates.add(cb.equal(root.get("seller"), seller));
		}
		if (available == true) {
			predicates.add(cb.greaterThan(root.get("numberAvailable"), 0));
		}
		if (ids != null) {
			cq.where(cb.or(predicates.toArray(new Predicate[] {})));
		} else {
			predicates.add(cb.equal(root.get("enabled"), enabled));
			cq.where(cb.and(predicates.toArray(new Predicate[] {})));
		}

		return em.createQuery(cq).getResultList();
	}
}
