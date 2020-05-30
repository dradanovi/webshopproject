package hr.java.web.radanovic.webShop.repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import hr.java.web.radanovic.webShop.enums.Authorities;
import hr.java.web.radanovic.webShop.model.AppRole;

@Repository
@Transactional
public class HibAppRoleRep implements RepAppRole {

	@PersistenceContext
	private EntityManager em;

	public AppRole findByAuthority(Authorities authority) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<AppRole> cq = cb.createQuery(AppRole.class);
		Root<AppRole> root = cq.from(AppRole.class);
		cq.where(cb.equal(root.get("authority"), authority));

		try {
			return em.createQuery(cq).getSingleResult();
		} catch (NoResultException e) {
			e.printStackTrace();
			return new AppRole();
		}
	}

}
