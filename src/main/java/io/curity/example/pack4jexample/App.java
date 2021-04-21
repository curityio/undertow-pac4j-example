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

import io.undertow.Undertow;
import io.undertow.server.RoutingHandler;
import io.undertow.server.session.InMemorySessionManager;
import io.undertow.server.session.SessionAttachmentHandler;
import io.undertow.server.session.SessionCookieConfig;
import org.pac4j.core.config.Config;
import org.pac4j.oidc.client.OidcClient;
import org.pac4j.oidc.config.OidcConfiguration;
import org.pac4j.undertow.handler.CallbackHandler;

public class App {
    public static void main(String[] args) {

        Configuration appConfiguration = new Configuration();
        OidcConfiguration oidcConfiguration = new OidcConfiguration();
        oidcConfiguration.setClientId(appConfiguration.getClientId());
        oidcConfiguration.setSecret(appConfiguration.getSecret());
        oidcConfiguration.setDiscoveryURI(appConfiguration.getDiscoveryURI());
        oidcConfiguration.setResponseType(appConfiguration.getResponseType());
        oidcConfiguration.setScope(appConfiguration.getScope());
        oidcConfiguration.setUseNonce(true);
        oidcConfiguration.setWithState(true);

        OidcClient<OidcConfiguration> oidcClient = new OidcClient<>(oidcConfiguration);
        oidcClient.setName("curity-client");

        Config config = new Config("http://localhost:8080/callback", oidcClient);

        LoginHandler loginHandler = new LoginHandler(config);
        UserHandler userHandler = new UserHandler(loginHandler);

        RoutingHandler routes = new RoutingHandler()
                .get("/login", loginHandler)
                .get("/callback", CallbackHandler.build(config, "/"))
                .get("/", userHandler)
                .setFallbackHandler(exchange -> exchange.setStatusCode(404));

        Undertow server = Undertow.builder()
                .addHttpListener(8080, "localhost",
                        new SessionAttachmentHandler(routes, new InMemorySessionManager("session-manager"), new SessionCookieConfig()))
                .build();
        server.start();
    }
}
