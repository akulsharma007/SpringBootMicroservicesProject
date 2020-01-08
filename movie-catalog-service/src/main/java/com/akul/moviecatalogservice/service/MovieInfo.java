package com.akul.moviecatalogservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.akul.moviecatalogservice.models.CatalogItem;
import com.akul.moviecatalogservice.models.Movie;
import com.akul.moviecatalogservice.models.Rating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.ribbon.proxy.annotation.Hystrix;

@Service
public class MovieInfo {
	@Autowired
	public RestTemplate restTemplate;

	@HystrixCommand(
			fallbackMethod = "getFallbackCatalogItem",
			threadPoolKey = "movieInfoPool",
			threadPoolProperties = {
					@HystrixProperty(name = "coreSize", value = "20"),
					@HystrixProperty(name = "maxQueueSize", value = "10")
				}
			)
	public CatalogItem getCatalogItem(Rating rating) {
		Movie movie = restTemplate.getForObject("http://movie-info-service/movies/"+rating.getMovieId(), Movie.class);
		return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
	}
	
	public static CatalogItem getFallbackCatalogItem(Rating rating){
		return new CatalogItem("No Movie found","",rating.getRating());
	}
}