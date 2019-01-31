package code.haotran.testing.data.remote.api;

import code.haotran.testing.BuildConfig;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Interceptor used to add API Key to the http request
 * <p>
 * @author Hao Tran
 */
public class AuthInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();

        HttpUrl url = request.url().newBuilder()
                .addQueryParameter("key", BuildConfig.SOF_KEY)
                .build();

        request = request.newBuilder().url(url).build();
        return chain.proceed(request);
    }
}
