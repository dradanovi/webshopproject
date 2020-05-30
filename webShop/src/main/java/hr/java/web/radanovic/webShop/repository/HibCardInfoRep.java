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

import hr.java.web.radanovic.webShop.model.CardInfo;
import hr.java.web.radanovic.webShop.model.Wallet;

@Repository
@Transactional
public class HibCardInfoRep implements RepCardInfo {

	@PersistenceContext
	private EntityManager em;

	@Override
	public <S extends CardInfo> S save(S entity) {
		em.persist(entity);
		return entity;
	}

	@Override
	public <S extends CardInfo> Iterable<S> saveAll(Iterable<S> entities) {
		entities.forEach(e -> em.persist(e));
		return entities;
	}

	@Override
	public Optional<CardInfo> findById(Long id) {
		return Optional.ofNullable(em.find(CardInfo.class, id));
	}

	@Override
	public boolean existsById(Long id) {
		if (em.find(CardInfo.class, id) != null) {
			return true;
		}
		return false;
	}

	@Override
	public Iterable<CardInfo> findAll() {
		return em.createQuery("from CardInfo", CardInfo.class).getResultList();
	}

	@Override
	public Iterable<CardInfo> findAllById(Iterable<Long> ids) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<CardInfo> cq = cb.createQuery(CardInfo.class);
		Root<CardInfo> root = cq.from(CardInfo.class);
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
	public void delete(CardInfo entity) {
		em.remove(entity);
	}

	@Override
	public void deleteAll(Iterable<? extends CardInfo> entities) {
		entities.forEach(e -> em.remove(e));

	}

	@Override
	public void deleteAll() {
		findAll().forEach(e -> em.remove(e));
	}

	@Override
	public List<CardInfo> findByWallet(Wallet wallet) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<CardInfo> cq = cb.createQuery(CardInfo.class);
		Root<CardInfo> root = cq.from(CardInfo.class);
		cq.where(cb.equal(root.get("wallet"), wallet));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public Optional<CardInfo> findByCardNumber(String cardNumber) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<CardInfo> cq = cb.createQuery(CardInfo.class);
		Root<CardInfo> root = cq.from(CardInfo.class);
		cq.where(cb.equal(root.get("cardNumber"), cardNumber));
		
		try {
			return Optional.ofNullable(em.createQuery(cq).getSingleResult());
		} catch (NoResultException e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}

}
