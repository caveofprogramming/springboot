package com.caveofprogramming.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.caveofprogramming.model.dto.SearchResult;
import com.caveofprogramming.model.entity.Profile;
import com.caveofprogramming.model.entity.SiteUser;
import com.caveofprogramming.model.repository.ProfileDao;

@Service
public class SearchService {

	private static final Logger logger = LoggerFactory.getLogger(SearchService.class);
	
	@Value("${results.pagesize}")
	private int pageSize;
	
	@Autowired
	private ProfileDao profileDao;

	public Page<SearchResult> search(String text, int pageNumber, SiteUser user) {

		if(pageNumber == 0) {
			pageNumber=1;
		}
		
		PageRequest request = PageRequest.of(pageNumber-1, pageSize);
		
		Page<Profile> results = null;
		
		// There is no user or user ID if the user is not logged in.
		Long userId = user == null ? -1: user.getId();
		
		if(user == null) {
			
		}
		
		if(!text.matches(".*\\w.*")) {
			logger.debug("Finding all....");
			results = profileDao.findAllByUserIdNot(userId, request);
		}
		else {
			logger.debug("Searching on '" + text + "'");
			results = profileDao.findAllByUserIdNotAndInterestsNameContainingIgnoreCase(userId, text, request);
		}
		
		
		return results.map(p -> new SearchResult(p));
	}
}
