/*
 *  Copyright 2021 Curity AB
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package io.curity.example.pack4jexample;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import org.pac4j.core.context.WebContext;
import org.pac4j.oidc.profile.OidcProfile;
import org.pac4j.undertow.context.UndertowWebContext;

import java.util.HashMap;
import java.util.Optional;

import static org.pac4j.core.util.Pac4jConstants.USER_PROFILES;

public class UserHandler implements HttpHandler
{
    private final HttpHandler _loginHandler;

    public UserHandler(HttpHandler loginHandler)
    {
        _loginHandler = loginHandler;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception
    {
        WebContext context = new UndertowWebContext(exchange);

        Optional<HashMap<String, OidcProfile>> optionalUserProfile = context.getSessionStore().get(context, USER_PROFILES);

        if (optionalUserProfile.isPresent()) {
            OidcProfile profile = optionalUserProfile.get().get("curity-client");
            exchange.getResponseHeaders().put(HttpString.tryFromString("Content-Type"), "application/json");
            String response = "{" +
                    "\"subject\": \"" + profile.getSubject() + "\"," +
                    "\"accessToken\": \"" + profile.getAccessToken() + "\"" +
                    "}";
            exchange.getResponseSender().send(response);
        } else {
            _loginHandler.handleRequest(exchange);
        }
    }
}
