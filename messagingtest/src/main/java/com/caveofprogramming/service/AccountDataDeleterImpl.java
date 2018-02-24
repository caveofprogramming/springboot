package com.caveofprogramming.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.caveofprogramming.model.custom.AccountDataDeleter;

@Service
public class AccountDataDeleterImpl implements AccountDataDeleter {

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public void deleteUserById(Long id) {

		System.err.println("2. Deleting ..... ");

		entityManager.createQuery("delete from VerificationToken v where v.user.id = :id").setParameter("id", id).executeUpdate();
		
		System.err.println("3. Deleting ..... ");
		
		entityManager.createQuery("delete from Message m where m.toUser.id = :id or m.fromUser.id=:id").setParameter("id", id).executeUpdate();
		
		System.err.println("4. Deleting ..... ");
		
		entityManager.createQuery("delete from Profile p where p.user.id = :id").setParameter("id", id).executeUpdate();
		
		System.err.println("5. Deleting ..... ");
		
		entityManager.createQuery("delete from SiteUser u where u.id = :id").setParameter("id", id).executeUpdate();
		
		System.err.println("6. Finished Deleting");
		/*
		 * List childIds = entityManager.createQuery(
		 * "select c.id from ChildEntity c where c.rootEntity.id = :pid"
		 * ).setParameter("pid", id).getResultList();
		 * 
		 * for(Long childId : childIds) { entityManager.createQuery(
		 * "delete from ChildEntity c where c.id = :id").setParameter("id",
		 * childId).executeUpdate(); } entityManager.createQuery(
		 * "delete from RootEntity r where r.id = :id").setParameter("id",
		 * id).executeUpdate();
		 */

	}

}
