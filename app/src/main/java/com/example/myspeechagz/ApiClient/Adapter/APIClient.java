package com.example.myspeechagz.ApiClient.Adapter;

import android.content.Context;

import com.example.myspeechagz.ApiClient.BuildConfig;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    private static Retrofit retrofit = null;
    APIClient apiClient;
    public static Retrofit getClient(Context context, RequestMethod requestMethod) {
        try {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.GEMINI_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getHttp(context,requestMethod))

                    .build();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }


        return retrofit;
    }

    public static OkHttpClient getHttp(Context context, RequestMethod requestMethod) throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if (!BuildConfig.DEBUG)
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        else
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        X509TrustManager trustManager = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] xcs, String string)
                    throws CertificateException {}
            public void checkServerTrusted(X509Certificate[] xcs, String string)
                    throws CertificateException {}
            public X509Certificate[] getAcceptedIssuers() {
                //Here
                return new X509Certificate[]{};
            }
        };

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient = new OkHttpClient.Builder()
                // Old ones
                .connectTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)

                .addInterceptor(interceptor)
                .addInterceptor(new APIinterceptor(context,requestMethod))
//                .addInterceptor(new ResponseInterceptor(context))
                //  .sslSocketFactory(new TLSSocketFactory(context,trustManager),trustManager)
                .hostnameVerifier((hostname,session)->true).build();

        return okHttpClient;

    }


}
