# Example Java website with OIDC login

[![Quality](https://img.shields.io/badge/quality-demo-red)](https://curity.io/resources/code-examples/status/)
[![Availability](https://img.shields.io/badge/availability-source-blue)](https://curity.io/resources/code-examples/status/)

This repository is an example Java application which performs an OpenID Connect login to get ID and access tokens from an
Authorization Server.

## Running the example

To run the example follow these steps:

- Edit the configuration in the `Configuration` class to reflect your setup.
- Start the project by running the command `./gradlew bootRun`.
- Navigate to [http://localhost:8080](http://localhost:8080), you will be redirected to your Authorization Server to perform login.
- Once the login is complete, you will see the username and access token returned as JSON.

## More information

Please visit [curity.io](https://curity.io/) for more information about the Curity Identity Server.
