package com.caveofprogramming.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caveofprogramming.model.Profile;
import com.caveofprogramming.model.ProfileDao;
import com.caveofprogramming.model.SiteUser;

@Service
public class ProfileService {
	
	@Autowired
	ProfileDao profileDao;
	
	public void save(Profile profile) {
		profileDao.save(profile);
	}
	
	public Profile getUserProfile(SiteUser user) {
		return profileDao.findByUser(user);
	}
}
