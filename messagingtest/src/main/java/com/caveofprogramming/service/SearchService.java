package com.caveofprogramming.service;

import com.caveofprogramming.model.dto.SearchResult;
import com.caveofprogramming.model.entity.Profile;
import com.caveofprogramming.model.repository.ProfileDao;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class SearchService {

	private static final Logger logger = LoggerFactory.getLogger(SearchService.class);
	
	@Value("${results.pagesize}")
	private int pageSize;
	
	@Autowired
	private ProfileDao profileDao;

	public Page<SearchResult> search(String text, int pageNumber) {

		if(pageNumber == 0) {
			pageNumber=1;
		}
		
		PageRequest request = new PageRequest(pageNumber-1, pageSize);
		
		Page<Profile> results = null;
		
		if(!text.matches(".*\\w.*")) {
			logger.debug("Finding all....");
			results = profileDao.findAll(request);
		}
		else {
			logger.debug("Searching on '" + text + "'");
			results = profileDao.findByInterestsNameContainingIgnoreCase(text, request);
		}
		
		Converter<Profile, SearchResult> converter = new Converter<Profile, SearchResult>() {
			public SearchResult convert(Profile profile) {
				return new SearchResult(profile);
			}
			
		};
		
		return results.map(converter);
	}
}
