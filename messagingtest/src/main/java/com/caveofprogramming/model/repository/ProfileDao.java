package com.caveofprogramming.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.caveofprogramming.model.entity.Profile;
import com.caveofprogramming.model.entity.SiteUser;

public interface ProfileDao extends CrudRepository<Profile, Long> {	
	Profile findByUser(SiteUser user);

	//List<Profile> findByInterestsNameContainingIgnoreCase(String text);
	//Page<Profile> findByInterestsNameContainingIgnoreCaseAndUserIdNot(Long userId, String text, Pageable request);
	Page<Profile> findAllByUserIdNot(Long id, Pageable request);
	Page<Profile> findAllByUserIdNotAndInterestsNameContainingIgnoreCase(Long userId, String text, Pageable request);
}
