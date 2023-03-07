package com.nukedemo.core.services.nominatim.model;

import java.util.List;
import lombok.Data;

@Data
public class NominatimResponse {
	private List<NominatimItem> response;
}