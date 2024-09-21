package com.geoSearchLocatio.model;

import lombok.Data;

@Data
public class NearestHospitalDetailsReqDTO {
	private String latitude;
	private String longitude;
	private String radius;
	private String token;

}
