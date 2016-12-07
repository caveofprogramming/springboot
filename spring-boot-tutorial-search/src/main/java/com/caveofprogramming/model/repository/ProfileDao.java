package com.caveofprogramming.model.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.caveofprogramming.model.entity.Profile;
import com.caveofprogramming.model.entity.SiteUser;

public interface ProfileDao extends CrudRepository<Profile, Long> {	
	Profile findByUser(SiteUser user);

	List<Profile> findByInterestsName(String text);
}
