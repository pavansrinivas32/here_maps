package com.geoSearchLocatio.controller;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.geoSearchLocatio.utils.CommonConstants;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/map")
@Slf4j
@SuppressWarnings("rawtypes")
public class MyIndiaMapsGetToken {

//	@RequestMapping(value = "/get-Token", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_FORM_URLENCODED)
	@GetMapping("/get-Token")
	public Object getAccessToken() {
		// Create a RestTemplate instance
		RestTemplate restTemplate = new RestTemplate();

		// Set the Content-Type header for the request
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		// Create the request body with the query parameters
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", CommonConstants.grantType);
		body.add("client_id", CommonConstants.clientId);
		body.add("client_secret", CommonConstants.clientSecret);

		// Create the HttpEntity with headers and body
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

		// Make the API POST request
		ResponseEntity<String> response;
		try {
			response = restTemplate.exchange(new URI(CommonConstants.baseUrl), HttpMethod.POST, requestEntity,
					String.class);
			log.info("Token is generated::::::::::::::::::::" + response.getBody());
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return "Error occurred while making the API request.";
		}

		// Return the response from the API
		return response.getBody();
	}
}
