// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.fcmjava.client.interceptors.request;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

public class AuthenticationRequestInterceptor implements HttpRequestInterceptor {

    private final String apiKey;

    public AuthenticationRequestInterceptor(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
        httpRequest.addHeader("Authorization", String.format("key=%s", apiKey));
    }
}
