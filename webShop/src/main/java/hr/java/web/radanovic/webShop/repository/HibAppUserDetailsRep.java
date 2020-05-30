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
import hr.java.web.radanovic.webShop.model.AppUserDetails;

@Repository
@Transactional
public class HibAppUserDetailsRep implements RepAppUserDetails {

	@PersistenceContext
	private EntityManager em;

	@Override
	public <S extends AppUserDetails> S save(S entity) {
		em.persist(entity);
		return entity;
	}

	@Override
	public <S extends AppUserDetails> Iterable<S> saveAll(Iterable<S> entities) {
		entities.forEach(e -> em.persist(e));
		return entities;
	}

	@Override
	public Optional<AppUserDetails> findById(Long id) {
		return Optional.ofNullable(em.find(AppUserDetails.class, id));
	}

	@Override
	public boolean existsById(Long id) {
		if (em.find(AppUserDetails.class, id) != null) {
			return true;
		}
		return false;
	}

	@Override
	public Iterable<AppUserDetails> findAll() {
		return em.createQuery("from AppUserRepository", AppUserDetails.class).getResultList();
	}

	@Override
	public Iterable<AppUserDetails> findAllById(Iterable<Long> ids) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<AppUserDetails> cq = cb.createQuery(AppUserDetails.class);
		Root<AppUserDetails> root = cq.from(AppUserDetails.class);
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
		em.remove(em.find(AppUserDetails.class, id));
	}

	@Override
	public void delete(AppUserDetails entity) {
		em.remove(entity);

	}

	@Override
	public void deleteAll(Iterable<? extends AppUserDetails> entities) {
		entities.forEach(e -> em.remove(e));

	}

	@Override
	public void deleteAll() {
		findAll().forEach(e -> em.remove(e));

	}

	@Override
	public Optional<AppUserDetails> findByUser(AppUser user) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<AppUserDetails> cq = cb.createQuery(AppUserDetails.class);
		Root<AppUserDetails> root = cq.from(AppUserDetails.class);
		cq.where(cb.equal(root.get("user"), user));
		try {
			return Optional.ofNullable(em.createQuery(cq).getSingleResult());
		} catch (NoResultException e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}
}
