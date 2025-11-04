package soa.study.flat_service.service;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import soa.study.flat_service.rest.dto.FlatStatResponse;

import javax.net.ssl.*;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class PushService {

    @Value("${agency.url}")
    private String agencyUrl;

    private final OkHttpClient httpClient;
    private final Gson gson = new Gson();

    public PushService() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                        public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            httpClient = new OkHttpClient.Builder()
                    .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                    .hostnameVerifier((hostname, session) -> true)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void push(Integer flatID, Integer roomNumbers) {
        try {
            FlatStatResponse flatStatResponse = new FlatStatResponse();
            flatStatResponse.setFlatId(flatID != null ? flatID.longValue() : null);
            flatStatResponse.setNumberOfRooms(roomNumbers != null ? roomNumbers : -1); // -1 = deleted
            flatStatResponse.setPrice(99f);

            String json = gson.toJson(flatStatResponse);

            RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

            Request request = new Request.Builder()
                    .url(agencyUrl)
                    .post(body)
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    log.warn("Failed to push FlatStat: HTTP " + response.code());
                } else {
                    log.info("FlatStat отправлен на агентство: {}", flatID);
                }
            }

        } catch (Exception e) {
            log.error("Error pushing FlatStat", e);
        }
    }
}
