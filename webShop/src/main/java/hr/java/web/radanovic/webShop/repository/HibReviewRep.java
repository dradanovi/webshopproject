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

import hr.java.web.radanovic.webShop.model.AppUser;
import hr.java.web.radanovic.webShop.model.Product;
import hr.java.web.radanovic.webShop.model.Review;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@Transactional
public class HibReviewRep implements RepReviews {

	@PersistenceContext
	private EntityManager em;

	@Override
	public <S extends Review> S save(S entity) {
		em.persist(entity);
		return entity;
	}

	@Override
	public <S extends Review> Iterable<S> saveAll(Iterable<S> entities) {
		entities.forEach(e -> em.persist(e));
		return entities;
	}

	@Override
	public Optional<Review> findById(Long id) {
		return Optional.ofNullable(em.find(Review.class, id));
	}

	@Override
	public boolean existsById(Long id) {
		if (em.find(Review.class, id) != null) {
			return true;
		}
		return false;
	}

	@Override
	public Iterable<Review> findAll() {
		return em.createQuery("from Review", Review.class).getResultList();
	}

	@Override
	public Iterable<Review> findAllById(Iterable<Long> ids) {
		return queryBuilder(null, null, null, ids);
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
	public void delete(Review entity) {
		em.remove(entity);
	}

	@Override
	public void deleteAll(Iterable<? extends Review> entities) {
		entities.forEach(e -> em.remove(e));
	}

	@Override
	public void deleteAll() {
		findAll().forEach(e -> em.remove(e));
	}

	@Override
	public List<Review> findByUser(AppUser user) {
		return queryBuilder(user, null, null, null);
	}

	@Override
	public List<Review> findByProduct(Product product) {
		log.info("find by product " + product.getId());
		return queryBuilder(null, product, null, null);
	}

	public List<Review> findAllByListProducts(List<Product> sellerProducts) {
		return queryBuilder(null, null, sellerProducts, null);
	}

	public boolean userReviewExists(AppUser user, Product product) {
		log.info("find by product user bool " + product.getId() + " " + user.getId());
		if (queryBuilder(user, product, null, null).size() == 0) {
			return false;
		} else
			return true;
	}

	public Review findUserProductReview(AppUser user, Product product) {
		log.info("find by product user " + product.getId() + " " + user.getId());
		List<Review> list = queryBuilder(user, product, null, null);
		if (list.size() > 0) {
			return list.get(0);
		} else
			return new Review();
	}
	
	public List<Review> findBestForMain(){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Review> cq = cb.createQuery(Review.class);
		Root<Review> root = cq.from(Review.class);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(cb.greaterThanOrEqualTo(root.get("grade"), 4));
		cq.orderBy(cb.desc(root.get("grade"))).where(cb.and(predicates.toArray(new Predicate[] {})));
		return em.createQuery(cq).setMaxResults(5).getResultList();
	}

	private List<Review> queryBuilder(AppUser user, Product product, List<Product> productList, Iterable<Long> ids) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Review> cq = cb.createQuery(Review.class);
		Root<Review> root = cq.from(Review.class);
		List<Predicate> predicates = new ArrayList<>();
		if (user != null) {
			predicates.add(cb.equal(root.get("user"), user));
		}
		if (product != null) {
			predicates.add(cb.equal(root.get("product"), product));
		}
		if (productList != null) {
			productList.forEach(e -> predicates.add(cb.equal(root.get("product"), e)));
		}
		if (ids != null) {
			ids.forEach(e -> predicates.add(cb.equal(root.get("id"), e)));
		}
		if (ids != null || productList != null) {
			cq.where(cb.or(predicates.toArray(new Predicate[] {})));
		} else {
			cq.where(cb.and(predicates.toArray(new Predicate[] {})));
		}

		return em.createQuery(cq).getResultList();
	}

}
