package util;

import dto.CryptoManagerRequestDTO;
import dto.CryptoManagerResponseDTO;
import dto.RequestWrapper;
import dto.ResponseWrapper;
import io.mosip.kernel.auth.defaultadapter.config.LoggerConfiguration;
//import io.mosip.kernel.core.logger.spi.Logger;
import org.apache.commons.codec.binary.Base64;
//import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

//import io.mosip.preregistration.core.common.dto.CryptoManagerRequestDTO;
//import io.mosip.preregistration.core.common.dto.CryptoManagerResponseDTO;
//import io.mosip.preregistration.core.common.dto.RequestWrapper;
//import io.mosip.preregistration.core.common.dto.ResponseWrapper;

/**
 * @author Tapaswini Behera
 * @since 1.0.0
 *
 */
@Service
public class CryptoUtil {

	//private Logger log = LoggerConfiguration.logConfig(CryptoUtil.class);


	//@Qualifier("selfTokenRestTemplate")
	@Autowired
	RestTemplate restTemplate = new RestTemplate();

	@Value("${cryptoResource.url}")
	public String cryptoResourceUrl;
	
	@Value("${preregistration.crypto.applicationId}")
	public String cryptoApplcationId;
	
	@Value("${preregistration.crypto.referenceId}")
	public String cryptoReferenceId;
	
	@Value("${preregistration.crypto.PrependThumbprint}")
	public boolean cryptoPrependThumbprint;




	public byte[] encrypt(byte[] originalInput, LocalDateTime localDateTime) {
		//log.info("sessionId", "idType", "id", "In encrypt method of CryptoUtil service ");

		ResponseEntity<ResponseWrapper<CryptoManagerResponseDTO>> response = null;
		byte[] encryptedBytes = null;
		try {
			String encodedBytes = io.mosip.kernel.core.util.CryptoUtil.encodeToURLSafeBase64(originalInput);
			CryptoManagerRequestDTO dto = new CryptoManagerRequestDTO();
			dto.setApplicationId("REGISTRATION");
			dto.setData(encodedBytes);
			dto.setReferenceId("");
			dto.setTimeStamp(localDateTime);
			dto.setPrependThumbprint(false);
			RequestWrapper<CryptoManagerRequestDTO> requestKernel = new RequestWrapper<>();
			requestKernel.setRequest(dto);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<RequestWrapper<CryptoManagerRequestDTO>> request = new HttpEntity<>(requestKernel, headers);
			//log.info("sessionId", "idType", "id",
					//"In encrypt method of CryptoUtil service cryptoResourceUrl: " + cryptoResourceUrl + "/encrypt");
			response = restTemplate.exchange("/encrypt", HttpMethod.POST, request,
					new ParameterizedTypeReference<ResponseWrapper<CryptoManagerResponseDTO>>() {
					});
			//log.info("sessionId", "idType", "id", "encrypt response of " + response);

			if (!(response.getBody().getErrors() == null || response.getBody().getErrors().isEmpty())) {
				//throw new EncryptionFailedException(response.getBody().getErrors(), null);
			}
			encryptedBytes = response.getBody().getResponse().getData().getBytes();

		} catch (Exception ex) {
//			log.debug("sessionId", "idType", "id", ExceptionUtils.getStackTrace(ex));
//			log.error("sessionId", "idType", "id",
//					"In encrypt method of CryptoUtil Util for Exception- " + ex.getMessage());
			throw ex;
		}
		return encryptedBytes;

	}

	public byte[] decrypt(byte[] originalInput, LocalDateTime localDateTime) {
//		log.info("sessionId", "idType", "id", "In decrypt method of CryptoUtil service ");
		ResponseEntity<ResponseWrapper<CryptoManagerResponseDTO>> response = null;
		byte[] decodedBytes = null;
		try {

			CryptoManagerRequestDTO dto = new CryptoManagerRequestDTO();
			dto.setApplicationId("REGISTRATION");
			dto.setData(new String(originalInput, StandardCharsets.UTF_8));
			dto.setReferenceId("INDIVIDUAL");
			dto.setTimeStamp(localDateTime);
			RequestWrapper<CryptoManagerRequestDTO> requestKernel = new RequestWrapper<>();
			requestKernel.setRequest(dto);

			HttpHeaders headers = new HttpHeaders();

			String token="eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJOdjdibklPVmJrakpTNjBSUzF4MW80dlk1SVBweU9LTVpzQWVheWd5TnVJIn0.eyJqdGkiOiI4ODFlN2NlMy0zYWJkLTQxYWEtOWEzNS1lZmY0ODhhMTkwMmEiLCJleHAiOjE2NDYxNTI5MjUsIm5iZiI6MCwiaWF0IjoxNjQ2MTE2OTI1LCJpc3MiOiJodHRwczovL2Rldi5tb3NpcC5uZXQva2V5Y2xvYWsvYXV0aC9yZWFsbXMvbW9zaXAiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiM2U0YmYyYjItZGFiMy00MDA3LTk5YWUtZDUwZDc0ZGE2MmZkIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoibW9zaXAtcHJlcmVnLWNsaWVudCIsImF1dGhfdGltZSI6MCwic2Vzc2lvbl9zdGF0ZSI6ImE3Yjc2ZTNlLTcwY2UtNGJlMS1iMDYzLTlkNzdkNmIyNzYxMyIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiaHR0cHM6Ly9kZXYubW9zaXAubmV0Il0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJSRUdJU1RSQVRJT05fUFJPQ0VTU09SIiwiUFJFUkVHIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsIlBSRV9SRUdJU1RSQVRJT05fQURNSU4iXX0sInJlc291cmNlX2FjY2VzcyI6eyJtb3NpcC1wcmVyZWctY2xpZW50Ijp7InJvbGVzIjpbInVtYV9wcm90ZWN0aW9uIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6ImVtYWlsIHByb2ZpbGUiLCJjbGllbnRIb3N0IjoiMTAuMjQ0LjcuMiIsImNsaWVudElkIjoibW9zaXAtcHJlcmVnLWNsaWVudCIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwicHJlZmVycmVkX3VzZXJuYW1lIjoic2VydmljZS1hY2NvdW50LW1vc2lwLXByZXJlZy1jbGllbnQiLCJjbGllbnRBZGRyZXNzIjoiMTAuMjQ0LjcuMiJ9.IOYTRGlt_uykr4CEX3V9SBNmpjXHDNnJZWtP-a8NufPdd9z96O2t6h-QE9Y9o9IMF_KJcMO1V1RIlTq74X_AXLzBeOdIEuQDHwEnc4Esu5HjBfwGpbhAOl8yE3SxPXp5DIbrX-IuuIPlJsrIMkEQYsUmzr6Zfx9AhAmF63Qbbe0ZwnIcRcKh3MDNPWB1IgsZAkdaNuls93q5PRhem0yJHpgk7VQH4v7pvqDhGSTAYEaFfdm94A1z1-z3p88WUwWobI5H4ioyADJ7eJzvFJx-HnQCSM0KElSirxl4kmPsmdYvlFFzsE0z8h3TOrcpqRU7a9Ffv5B6Zu8pbfzi5S2SUw";
			headers.set("Authorization", token);
//			HttpEntity<String> jwtEntity = new HttpEntity<String>(headers);
//			// Use Token to get Response
//			ResponseEntity<String> helloResponse = restTemplate.exchange(HELLO_URL, HttpMethod.GET, jwtEntity,
//					String.class);


			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<RequestWrapper<CryptoManagerRequestDTO>> request = new HttpEntity<>(requestKernel, headers);
//					"In decrypt method of CryptoUtil service cryptoResourceUrl: " + cryptoResourceUrl + "/decrypt");
//			log.info("sessionId", "idType", "id",
			System.out.println(request);
			response = restTemplate.exchange( "https://qa3.mosip.net/v1/keymanager/decrypt", HttpMethod.POST, request,
					new ParameterizedTypeReference<ResponseWrapper<CryptoManagerResponseDTO>>() {

					});
			if (!(response.getBody().getErrors() == null || response.getBody().getErrors().isEmpty())) {
				//throw new EncryptionFailedException(response.getBody().getErrors(), null);
			}
			decodedBytes = Base64.decodeBase64(response.getBody().getResponse().getData().getBytes());

		} catch (Exception ex) {
//			log.debug("sessionId", "idType", "id", ExceptionUtils.getStackTrace(ex));
//			log.error("sessionId", "idType", "id",
//					"In decrypt method of CryptoUtil Util for Exception- " + ex.getMessage());
			throw ex;
		}
		return decodedBytes;

	}

}
