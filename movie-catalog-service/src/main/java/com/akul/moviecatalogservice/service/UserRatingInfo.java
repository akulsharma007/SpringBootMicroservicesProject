package com.akul.moviecatalogservice.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.akul.moviecatalogservice.models.Rating;
import com.akul.moviecatalogservice.models.UserRating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Service
public class UserRatingInfo {

	@Autowired
	private RestTemplate restTemplate;
	
	@HystrixCommand(
			fallbackMethod = "getFallbackUserRating",
			threadPoolKey = "userRatingPool",
			threadPoolProperties = {
					@HystrixProperty(name = "coreSize", value = "20"),
					@HystrixProperty(name = "maxQueueSize", value = "10")
				}
			)
	public UserRating getUserRating(String id) {
		UserRating listObject = restTemplate.getForObject("http://ratings-data-service/ratingsdata/user/"+id, UserRating.class);
		return listObject;
	}
	
	public static UserRating getFallbackUserRating(String id){
		UserRating ur = new UserRating();
		ur.setUserId(id);
		ur.setUserRating(Arrays.asList(new Rating("0",0)));
		return ur;
	}
}
