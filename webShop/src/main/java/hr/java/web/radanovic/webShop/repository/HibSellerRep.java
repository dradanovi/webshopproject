package hr.java.web.radanovic.webShop.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import hr.java.web.radanovic.webShop.model.AppUser;
import hr.java.web.radanovic.webShop.model.Seller;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@Transactional
public class HibSellerRep implements RepSeller {

	@PersistenceContext
	private EntityManager em;

	@Override
	public <S extends Seller> S save(S entity) {
		log.info("persist " + entity.toString());
		em.persist(entity);
		return entity;
	}
	
	public <S extends Seller> S update(S entity) {
		log.info("merge " + entity.toString());
		em.merge(entity);
		return entity;
	}

	@Override
	public <S extends Seller> Iterable<S> saveAll(Iterable<S> entities) {
		entities.forEach(e -> em.persist(e));
		return entities;
	}

	@Override
	public Optional<Seller> findById(Long id) {
		return Optional.ofNullable(em.find(Seller.class, id));
	}

	@Override
	public boolean existsById(Long id) {
		if (em.find(Seller.class, id) != null) {
			return true;
		}
		return false;
	}

	public boolean existsByUser(AppUser user) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Seller> cq = cb.createQuery(Seller.class);
		Root<Seller> root = cq.from(Seller.class);
		cq.where(cb.equal(root.get("user"), user));
		if (em.createQuery(cq).getResultList().size() > 0)
			return true;
		else
			return false;

	}

	@Override
	public Iterable<Seller> findAll() {
		return em.createQuery("from Seller", Seller.class).getResultList();
	}

	@Override
	public Iterable<Seller> findAllById(Iterable<Long> ids) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Seller> cq = cb.createQuery(Seller.class);
		Root<Seller> root = cq.from(Seller.class);
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
	public void delete(Seller entity) {
		em.remove(entity);
	}

	@Override
	public void deleteAll(Iterable<? extends Seller> entities) {
		entities.forEach(e -> em.remove(e));
	}

	@Override
	public void deleteAll() {
		findAll().forEach(e -> em.remove(e));
	}

	@Override
	public Optional<Seller> findByUser(AppUser user) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Seller> cq = cb.createQuery(Seller.class);
		Root<Seller> root = cq.from(Seller.class);
		cq.where(cb.equal(root.get("user"), user));

		try {
			return Optional.ofNullable(em.createQuery(cq).getSingleResult());
		} catch (NoResultException e) {
			log.info("NoResultException -> seller by user");
			e.printStackTrace();
			return Optional.empty();
		}
	}
	
	public boolean recalculateRating() {
		StoredProcedureQuery spQuery = em.createNamedStoredProcedureQuery("recalculateratings");
		return spQuery.execute();
	}
}
