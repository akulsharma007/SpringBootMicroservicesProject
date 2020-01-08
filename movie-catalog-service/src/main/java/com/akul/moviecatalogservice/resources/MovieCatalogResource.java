package com.akul.moviecatalogservice.resources;

import com.akul.moviecatalogservice.models.CatalogItem;
import com.akul.moviecatalogservice.models.Movie;
import com.akul.moviecatalogservice.models.Rating;
import com.akul.moviecatalogservice.models.UserRating;
import com.akul.moviecatalogservice.service.MovieInfo;
import com.akul.moviecatalogservice.service.UserRatingInfo;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

	@Autowired
	private MovieInfo movieInfo;
	
	@Autowired
	private UserRatingInfo userRatingInfo;

	@RequestMapping("/{id}")
	public List<CatalogItem> getCatalog(@PathVariable("id") String id){
		UserRating listObject = userRatingInfo.getUserRating(id);
		return listObject.getUserRating().stream()
				.map(rating-> movieInfo.getCatalogItem(rating))
				.collect(Collectors.toList());
	}
}
