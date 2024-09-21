package com.geoSearchLocatio.controller;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.geoSearchLocatio.exceptions.InSufficientInputException;
import com.geoSearchLocatio.model.HereMapToken;
import com.geoSearchLocatio.model.Location;
import com.geoSearchLocatio.model.NearestHospitalDetailsReqDTO;
import com.geoSearchLocatio.model.Panshops;
import com.geoSearchLocatio.utils.CommonConst;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/heremaps")
@CrossOrigin
@Slf4j
@SuppressWarnings("rawtypes")
public class HeremapController {
	
	
	/**
	 * @author : pavan
	 * @throws SQLException
	 * @throws UnknownHostException
	 * @throws: DataNotFoundException
	 * @URL : http://localhost:8080/rms/get-Here-Map-Token
	 */
	@GetMapping("/get-tok")
	public Object getAccessToken() {
        RestTemplate restTemplate = new RestTemplate();

        HereMapToken generator = new HereMapToken(
        		CommonConst.here_Map_AccessKeyId,
        		CommonConst.here_Map_AccessKeySecret,
                "", ""
        );

        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("grant_type", "client_credentials");

        String header = generator.generateHeader("POST", "https://account.api.here.com/oauth2/token", requestParams);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization", header);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                "https://account.api.here.com/oauth2/token",
                HttpMethod.POST,
                formEntity,
                Object.class
        );

        log.info("data of token: " + response.getBody());
        return response.getBody();
    }
	
	/**
	 * @author : pavan
	 * @throws SQLException
	 * @throws UnknownHostException
	 * @throws: DataNotFoundException
	 * @URL : http://localhost:8080/rms/getLoctionBasedOnPlace
	 * @Parameters :
	 * @Inputs : { "itemname": "biryani", "restaurantname": "test", "price": "124",
	 * 
	 *         }
	 */
	@RequestMapping(value = "/getLoctionBasedOnPlace", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Object getLoctionBasedOnPlace(@RequestBody Location loc) throws InSufficientInputException {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + loc.getToken());
		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		HttpEntity formEntity = new HttpEntity<MultiValueMap<String, String>>(requestBody, headers);
		ResponseEntity<Object> response = restTemplate.exchange(
				"https://geocode.search.hereapi.com/v1/geocode?q=" + loc.getSearchKey(), HttpMethod.GET, formEntity,
				Object.class);
		System.out.println(response.getBody());
		return response.getBody();
	}

	/**
	 * @author : pavan
	 * @throws SQLException
	 * @throws UnknownHostException
	 * @throws: DataNotFoundException
	 * @URL : http://localhost:8080/rms/getNearestHospitalDetails
	 * @Parameters :
	 * @Inputs : { "token": "token", "latitude": "17.3730", "longitude": "78.5476",
	 *         "radius": "40000" }
	 */
	@RequestMapping(value = "/getNearestHospitalDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Object getNearestHospitalDetails(@RequestBody NearestHospitalDetailsReqDTO controllerDTO)
			throws InSufficientInputException {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + controllerDTO.getToken());
		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		HttpEntity formEntity = new HttpEntity<MultiValueMap<String, String>>(requestBody, headers);
		ResponseEntity<Object> response = restTemplate
				.exchange("https://autosuggest.search.hereapi.com/v1/autosuggest?&lang=en&in=circle:"
						+ controllerDTO.getLatitude() + "," + controllerDTO.getLongitude() + ";r="
						+ controllerDTO.getRadius() + "&q=hosp", HttpMethod.GET, formEntity, Object.class);
		System.out.println(response.getBody());
		return response.getBody();
	}

	/**
	 * @author : pavan
	 * @throws SQLException
	 * @throws UnknownHostException
	 * @throws: DataNotFoundException
	 * @URL : http://localhost:8080/rms/getNearestBloodBanks
	 * @Parameters :
	 * @Inputs : { "token": "token", "latitude": "17.3730", "longitude": "78.5476",
	 *         "radius": "40000" }
	 */
	@RequestMapping(value = "/getNearestBloodBanks", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Object getNearestBloodBanks(@RequestBody NearestHospitalDetailsReqDTO controllerDTO)
			throws InSufficientInputException {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + controllerDTO.getToken());
		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		HttpEntity formEntity = new HttpEntity<MultiValueMap<String, String>>(requestBody, headers);
		ResponseEntity<Object> response = restTemplate
				.exchange(
						"https://autosuggest.search.hereapi.com/v1/autosuggest?&limit=30&lang=en&in=circle:"
								+ controllerDTO.getLatitude() + "," + controllerDTO.getLongitude() + ";r="
								+ controllerDTO.getRadius() + "&q=bloodbanks",
						HttpMethod.GET, formEntity, Object.class);
		System.out.println(response.getBody());
		return response.getBody();
	}

	/**
	 * @author : pavan
	 * @throws SQLException
	 * @throws UnknownHostException
	 * @throws: DataNotFoundException
	 * @URL : http://localhost:8080/rms/getNearestBloodBanks
	 * @Parameters :
	 * @Inputs : { "token": "token", "latitude": "17.3730", "longitude": "78.5476",
	 *         "radius": "40000" }
	 */

//	@RequestMapping(value = "/getShoppingMalls", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping("/shopmalls")
	public Object getShoppingMalls(@RequestBody NearestHospitalDetailsReqDTO controllerDTO)
			throws InSufficientInputException {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + controllerDTO.getToken());
		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		HttpEntity formEntity = new HttpEntity<MultiValueMap<String, String>>(requestBody, headers);
		ResponseEntity<Object> response = restTemplate
				.exchange(
						"https://autosuggest.search.hereapi.com/v1/autosuggest?&limit=20&lang=en&in=circle:"
								+ controllerDTO.getLatitude() + "," + controllerDTO.getLongitude() + ";r="
								+ controllerDTO.getRadius() + "&q=shoppingmalls",
						HttpMethod.GET, formEntity, Object.class);
		System.out.println(response.getBody());
		return response.getBody();
	}
	
	
	/**
	 * @author : pavan
	 * @throws SQLException
	 * @throws UnknownHostException
	 * @throws: DataNotFoundException
	 * @URL : http://localhost:8080/rms/getNearestBloodBanks
	 * @Parameters :
	 * @Inputs : { "token": "token", "latitude": "17.3730", "longitude": "78.5476",
	 *         "radius": "40000" }
	 */

	@RequestMapping(value = "/getPetrolBunks", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Object getPetrolBunkDetails(@RequestBody NearestHospitalDetailsReqDTO controllerDTO)
			throws InSufficientInputException {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + controllerDTO.getToken());
		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		HttpEntity formEntity = new HttpEntity<MultiValueMap<String, String>>(requestBody, headers);
		ResponseEntity<Object> response = restTemplate
				.exchange(
						"https://autosuggest.search.hereapi.com/v1/autosuggest?&limit=50&lang=en&in=circle:"
								+ controllerDTO.getLatitude() + "," + controllerDTO.getLongitude() + ";r="
								+ controllerDTO.getRadius() + "&q=petrolbunks",
						HttpMethod.GET, formEntity, Object.class);
		System.out.println(response.getBody());
		return response.getBody();
	}

	/**
	 * @author : pavan
	 * @throws SQLException
	 * @throws UnknownHostException
	 * @throws: DataNotFoundException
	 * @URL : http://localhost:8080/rms/addressAutoSuggest
	 * @Parameters :
	 * @Inputs : { "token": "token", "searchKey": "lala" }
	 */
	@RequestMapping(value = "/addressAutoSuggest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Object addressAutoSuggest(@RequestBody Location loc) throws InSufficientInputException {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + loc.getToken());
//		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		HttpEntity<String> formEntity = new HttpEntity<String>(headers);
		ResponseEntity<Object> response = restTemplate.exchange(
				"https://autosuggest.search.hereapi.com/v1/autosuggest?at=0.00,0.00&lang=en&in=countryCode:IND&show=streetInfo&limit=20&q="
						+ loc.getSearchKey(),
				HttpMethod.GET, formEntity, Object.class);
		System.out.println(response.getBody());
		return response.getBody();
	}

	/**
	 * @author : pavan
	 * @throws SQLException
	 * @throws UnknownHostException
	 * @throws: DataNotFoundException
	 * @URL : http://localhost:8080/rms/addressAutoSuggest
	 * @Parameters :
	 * @Inputs : { "token": "token", "searchKey": "lala" }
	 */
	@RequestMapping(value = "/addressDiscover", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Object addressDiscover(@RequestBody Location loc) throws InSufficientInputException {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + loc.getToken());
		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		HttpEntity formEntity = new HttpEntity<MultiValueMap<String, String>>(requestBody, headers);
		ResponseEntity<Object> response = restTemplate
				.exchange("https://discover.search.hereapi.com/v1/discover?at=0.00,0.00&q=" + loc.getSearchKey()
						+ "&in=countryCode:IND", HttpMethod.GET, formEntity, Object.class);
		System.out.println(response.getBody());
		return response.getBody();
	}
	
	
	
	/**
	 * @author : pavan 
	 * @throws SQLException
	 * @throws UnknownHostException
	 * @throws: DataNotFoundException
	 * @URL : http://localhost:8080/rms/getRestaurantsDetails
	 * @Parameters :
	 * @Inputs : { "token": "token", "latitude": "17.3730", "longitude": "78.5476",
	 *         "radius": "40000" }
	 */

	@RequestMapping(value = "/getRestaurantsDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Object getRestaurantsDetails(@RequestBody NearestHospitalDetailsReqDTO controllerDTO)
			throws InSufficientInputException {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + controllerDTO.getToken());
		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		HttpEntity formEntity = new HttpEntity<MultiValueMap<String, String>>(requestBody, headers);
		ResponseEntity<Object> response = restTemplate
				.exchange(
						"https://autosuggest.search.hereapi.com/v1/autosuggest?&limit=50&lang=te&in=circle:"
								+ controllerDTO.getLatitude() + "," + controllerDTO.getLongitude() + ";r="
								+ controllerDTO.getRadius() + "&q=restaurants",
						HttpMethod.GET, formEntity, Object.class);
		System.out.println(response.getBody());
		return response.getBody();
	}
	
	/**
	 * @author : pavan 
	 * @throws SQLException
	 * @throws UnknownHostException
	 * @throws: DataNotFoundException
	 * @URL : http://localhost:8080/rms/getRestaurantsDetails
	 * @Parameters :
	 * @Inputs : { "token": "token", "latitude": "16.998855", "longitude": "81.77786",
	 *         "radius": "40000" }
	 */

	@RequestMapping(value = "/getPolicestationsDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Object getPolicestationsDetails(@RequestBody NearestHospitalDetailsReqDTO controllerDTO)
			throws InSufficientInputException {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + controllerDTO.getToken());
		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		HttpEntity formEntity = new HttpEntity<MultiValueMap<String, String>>(requestBody, headers);
		ResponseEntity<Object> response = restTemplate
				.exchange(
						"https://autosuggest.search.hereapi.com/v1/autosuggest?&limit=50&lang=te&in=circle:"
								+ controllerDTO.getLatitude() + "," + controllerDTO.getLongitude() + ";r="
								+ controllerDTO.getRadius() + "&q=Policestations",
						HttpMethod.GET, formEntity, Object.class);
		System.out.println(response.getBody());
		return response.getBody();
	}
	
	
	/**
	 * @author : pavan
	 * @throws SQLException
	 * @throws UnknownHostException
	 * @throws: DataNotFoundException
	 * @URL : http://localhost:8080/rms/getNearestBloodBanks
	 * @Parameters :
	 * @Inputs : { "token": "token", "latitude": "17.3730", "longitude": "78.5476",
	 *         "radius": "40000" }
	 */
	@RequestMapping(value = "/getPanShops", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Object getNearestPanShops(@RequestBody Panshops controllerDTO)
			throws InSufficientInputException {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + controllerDTO.getToken());
		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		HttpEntity formEntity = new HttpEntity<MultiValueMap<String, String>>(requestBody, headers);
		ResponseEntity<Object> response = restTemplate
				.exchange(
						"https://autosuggest.search.hereapi.com/v1/autosuggest?&limit=30&lang=en&in=circle:"
								+ controllerDTO.getLatitude() + "," + controllerDTO.getLongitude() + ";r="
								+ controllerDTO.getRadius() + "&q=panshops",
						HttpMethod.GET, formEntity, Object.class);
		System.out.println(response.getBody());
		return response.getBody();
	}



}
