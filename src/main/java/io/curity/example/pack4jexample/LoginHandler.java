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
import org.pac4j.core.config.Config;
import org.pac4j.core.exception.http.RedirectionAction;
import org.pac4j.oidc.client.OidcClient;
import org.pac4j.oidc.config.OidcConfiguration;
import org.pac4j.undertow.context.UndertowWebContext;
import org.pac4j.undertow.http.UndertowHttpActionAdapter;

import java.util.Optional;

public class LoginHandler implements HttpHandler
{
    private final OidcClient<OidcConfiguration> _oidcClient;

    public LoginHandler(Config config)
    {
        _oidcClient = (OidcClient<OidcConfiguration>) config.getClients().findClient("curity-client").get();
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception
    {
        UndertowWebContext context = new UndertowWebContext(exchange);

        Optional<RedirectionAction> action = _oidcClient.getRedirectionAction(context);

        UndertowHttpActionAdapter.INSTANCE.adapt(action.get(), context);
    }
}
