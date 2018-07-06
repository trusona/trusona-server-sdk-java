package com.trusona.sdk.http.client.interceptor;

import com.trusona.sdk.http.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class TrusonaHeaderInterceptor implements Interceptor {
  private static final String TRUSONA_USER_AGENT = "TrusonaServerSdk/1.0";
  private static final String DATE_FORMAT = "EEE, dd MMM YYYY  HH:mm:ss Z";
  private static final TimeZone GMT = TimeZone.getTimeZone("GMT");

  @Override
  public Response intercept(Chain chain) throws IOException {
    Request request = chain.request();

    return chain.proceed(request.newBuilder()
      .addHeader(Headers.USER_AGENT, TRUSONA_USER_AGENT)
      .addHeader(Headers.X_DATE, getDate())
      .build()
    );
  }

  private String getDate() {
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
    dateFormat.setTimeZone(GMT);

    return dateFormat.format(calendar.getTime());
  }
}
