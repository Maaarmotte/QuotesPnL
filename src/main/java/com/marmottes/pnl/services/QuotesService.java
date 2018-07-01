package com.marmottes.pnl.services;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.marmottes.pnl.Config;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import lombok.extern.slf4j.Slf4j;
import com.marmottes.pnl.UnsafeOkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class QuotesService {
    public static final String NO_VALUE_STRING = "-";

    private static final int MAX_CACHE_SIZE = 4096;
    private static OkHttpClient HTTP_CLIENT = UnsafeOkHttpClient.getUnsafeOkHttpClient();

    private final Config config;
    private final LoadingCache<String, QuoteDetails> cache;

    @Autowired
    public QuotesService(Config config) {
        this.config = config;
        this.cache = CacheBuilder.newBuilder()
                .maximumSize(MAX_CACHE_SIZE)
                .expireAfterWrite(30, TimeUnit.SECONDS)
                .build(
                        new CacheLoader<String, QuoteDetails>() {
                            @Override
                            public QuoteDetails load(@NonNull String s) {
                                return fetchQuoteDetails(s);
                            }
                        });
    }

    @Nullable
    public QuoteDetails getQuoteDetails(String isin) {
        if (isin == null) {
            return null;
        }

        QuoteDetails quoteDetails = null;

        try {
            quoteDetails = cache.getUnchecked(isin);
        }
        catch (UncheckedExecutionException e) {
            log.error("Couldn't fetch quote {}", isin, e);
        }

        return quoteDetails;
    }

    @Nullable
    private QuoteDetails fetchQuoteDetails(String isin) {
        log.info(String.format("Fetching quote %s", isin));

        Request request = new Request.Builder()
                .url(String.format(config.getUrl(), isin))
                .build();

        try {
            Response response = HTTP_CLIENT.newCall(request).execute();
            return new QuoteDetails(response.body().string());
        } catch (IOException e) {
            log.error("Couldn't fetch quote {}", isin, e);
        }

        return null;
    }

    public class QuoteDetails {
        private Map<String, String> properties;

        public QuoteDetails(String data) {
            this.properties = new HashMap<>();

            try {
                String[] lines = data.split("\n");
                String[] header = lines[0].split("\\|");
                String[] quote = lines[1].split("\\|");

                for (int i = 0; i < header.length; ++i) {
                    properties.put(header[i], quote[i]);
                }
            } catch (IndexOutOfBoundsException e) {
                log.error("Couldn't parse quote", e);
            }
        }

        public String get(String key) {
            return properties.get(key);
        }
    }
}
