package apps.janshakti.networking;

import android.util.Log;

import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String TAG = "RetrofitClient";
    private static RetrofitClient instance = null;
    private ApiInterface myApi;
    public static String BASE_URL = "http://janshakti.upsdc.gov.in/";

    //public static String BASE_URL = "https://dgme.uplive.in/";


    private RetrofitClient() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(5, TimeUnit.MINUTES)
                    .connectTimeout(5, TimeUnit.MINUTES)
                    .callTimeout(5, TimeUnit.MINUTES)
                    .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    }).build();

            Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL + "Api/")
                    .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient)
                    .build();
            myApi = retrofit.create(ApiInterface.class);


        } catch (Exception e) {
            Log.d(TAG, "RetrofitClient: " + e.getLocalizedMessage());
        }

    }

    public static ApiInterface getClient() {
        OkHttpClient okHttpClient2 = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.MINUTES)
                .callTimeout(5, TimeUnit.MINUTES)
                .connectTimeout(5, TimeUnit.MINUTES).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://attendancefaceapiptpl.cognitiveservices.azure.com/face/v1.0/")
                .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient2)
                .build();
        return retrofit.create(ApiInterface.class);
    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public ApiInterface getMyApi() {
        return myApi;
    }
}