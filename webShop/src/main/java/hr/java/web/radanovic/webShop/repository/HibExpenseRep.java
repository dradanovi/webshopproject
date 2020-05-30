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

import hr.java.web.radanovic.webShop.model.CardInfo;
import hr.java.web.radanovic.webShop.model.Expense;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@Transactional
public class HibExpenseRep implements RepExpense {

	@PersistenceContext
	private EntityManager em;

	@Override
	public <S extends Expense> S save(S entity) {
		em.persist(entity);
		return entity;
	}

	@Override
	public <S extends Expense> Iterable<S> saveAll(Iterable<S> entities) {
		entities.forEach(e -> em.persist(e));
		return entities;
	}

	@Override
	public Optional<Expense> findById(Long id) {
		return Optional.ofNullable(em.find(Expense.class, id));
	}

	@Override
	public boolean existsById(Long id) {
		if (em.find(Expense.class, id) != null) {
			return true;
		}
		return false;
	}

	@Override
	public Iterable<Expense> findAll() {
		return em.createQuery("from Expense", Expense.class).getResultList();
	}

	@Override
	public Iterable<Expense> findAllById(Iterable<Long> ids) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Expense> cq = cb.createQuery(Expense.class);
		Root<Expense> root = cq.from(Expense.class);
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
	public void delete(Expense entity) {
		em.remove(entity);
	}

	@Override
	public void deleteAll(Iterable<? extends Expense> entities) {
		entities.forEach(e -> em.remove(e));
	}

	@Override
	public void deleteAll() {
		findAll().forEach(e -> em.remove(e));
	}

	@Override
	public List<Expense> findByCardUsed(CardInfo cardUsed) {
		log.info("find card used");
		return queryBuilder(cardUsed);
	}

	public List<Expense> findUnpaid() {
		log.info("get unpaid");
		return queryBuilder(null);
	}
	
	private List<Expense> queryBuilder(CardInfo cardUsed){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Expense> cq = cb.createQuery(Expense.class);
		Root<Expense> root = cq.from(Expense.class);
		if(cardUsed != null) {
			cq.where(cb.equal(root.get("cardUsed"), cardUsed));
		} else if(cardUsed == null) {
			cq.where(cb.isNull(root.get("datePayed")));
		}
		return em.createQuery(cq).getResultList();
	}
}
