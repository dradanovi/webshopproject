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
import hr.java.web.radanovic.webShop.model.Wallet;

@Repository
@Transactional
public class HibWalletRep implements RepWallet {

	@PersistenceContext
	private EntityManager em;

	@Override
	public <S extends Wallet> S save(S entity) {
		em.persist(entity);
		return entity;
	}

	@Override
	public <S extends Wallet> Iterable<S> saveAll(Iterable<S> entities) {
		entities.forEach(e -> em.persist(e));
		return entities;
	}

	@Override
	public Optional<Wallet> findById(Long id) {
		return Optional.ofNullable(em.find(Wallet.class, id));
	}

	@Override
	public boolean existsById(Long id) {
			if(em.find(Wallet.class, id) != null) {
				return true;
			}
			return false;
	}

	@Override
	public Iterable<Wallet> findAll() {
		return em.createQuery("from Wallet", Wallet.class).getResultList();
	}

	@Override
	public Iterable<Wallet> findAllById(Iterable<Long> ids) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Wallet> cq = cb.createQuery(Wallet.class);
		Root<Wallet> root = cq.from(Wallet.class);
		List<Predicate> persistance = new ArrayList<>();
		ids.forEach(e -> persistance.add(cb.equal(root.get("id"), e)));
		cq.where(cb.or(persistance.toArray(new Predicate[] {})));
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
	public void delete(Wallet entity) {
		em.remove(entity);
	}

	@Override
	public void deleteAll(Iterable<? extends Wallet> entities) {
		entities.forEach(e -> em.remove(e));
	}

	@Override
	public void deleteAll() {
		findAll().forEach(e -> em.remove(e));
	}

	@Override
	public Wallet findByUser(AppUser user) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Wallet> cq = cb.createQuery(Wallet.class);
		Root<Wallet> root = cq.from(Wallet.class);
		cq.where(cb.equal(root.get("user"), user));

		try {
			return em.createQuery(cq).getSingleResult();
		} catch (NoResultException e) {
			e.printStackTrace();
			return new Wallet();
		}
	}

}
