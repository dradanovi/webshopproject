package hr.java.web.radanovic.webShop.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import hr.java.web.radanovic.webShop.model.AppUser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@Transactional
public class HibAppUserRep implements RepAppUser {

	@PersistenceContext
	private EntityManager em;

	@Override
	public <S extends AppUser> S save(S entity) {
		em.persist(entity);
		return entity;
	}

	@Override
	public <S extends AppUser> Iterable<S> saveAll(Iterable<S> entities) {
		entities.forEach(e -> em.persist(e));
		return entities;
	}

	@Override
	public Optional<AppUser> findById(Long id) {
		return Optional.ofNullable(em.find(AppUser.class, id));
	}

	@Override
	public boolean existsById(Long id) {
		if (em.find(AppUser.class, id) != null) {
			return true;
		}
		return false;
	}

	@Override
	public Iterable<AppUser> findAll() {
		return em.createQuery("from AppUser", AppUser.class).getResultList();
	}

	@Override
	public Iterable<AppUser> findAllById(Iterable<Long> ids) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<AppUser> cq = cb.createQuery(AppUser.class);
		Root<AppUser> root = cq.from(AppUser.class);
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
	public void delete(AppUser entity) {
		em.remove(entity);
	}

	@Override
	public void deleteAll(Iterable<? extends AppUser> entities) {
		entities.forEach(e -> em.remove(e));
	}

	@Override
	public void deleteAll() {
		findAll().forEach(e -> em.remove(e));
	}

	@Override
	public Optional<AppUser> findByUsername(String username) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<AppUser> cq = cb.createQuery(AppUser.class);
		Root<AppUser> root = cq.from(AppUser.class);
		cq.where(cb.equal(root.get("username"), username));
		try {
			return Optional.ofNullable(em.createQuery(cq).getSingleResult());
		} catch (NoResultException e) {
			log.info("Username not found");
			return Optional.empty();
		}
	}

}
