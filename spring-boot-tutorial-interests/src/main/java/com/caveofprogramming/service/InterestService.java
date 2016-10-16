package com.caveofprogramming.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caveofprogramming.model.Interest;
import com.caveofprogramming.model.InterestDao;

@Service
public class InterestService {
	@Autowired
	private InterestDao interestDao;
	
	public long count() {
		return interestDao.count();
	}
	
	public Interest get(String interestName) {
		return interestDao.findOneByName(interestName);
	}
	
	public void save(Interest interest) {
		interestDao.save(interest);
	}
	
	public Interest createIfNotExists(String interestName) {
		Interest interest = interestDao.findOneByName(interestName);
		
		if(interest == null) {
			interest = interestDao.save(interest);
		}
		
		return interest;
	}
}
