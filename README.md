# Chat app using Spring Boot + Auth0 + Ably

This is a demo showing how to make a simple site with Spring Boot and Auth0 for hosting chat app, which can be used live by anyone anywhere.

For details on how this app was created and any other specifics, check out the [blog](https://www.ably.com/blog) on it.

## Usage

To get this running:

1. Download this repo
1. [Create an Auth0 account](https://auth0.com/), and get your client ID, client Secret, and Issuer URI. Add these to your application.yml file
1. On your Auth0 app, set the app's *Allowed Callback URLs* to `http://localhost:8080/login/oauth2/code/auth0`, and your app's *Allowed Logout URLs* to `http://localhost:8080/`
1. [Create an Ably account](https://ably.com/signup). Get an [API key](https://ably.com/accounts/any/apps/any/app_keys) from one of your Ably Apps. Add this to your application.properties file
1. Run `./mvnw spring-boot:run` in the base of this repo's directory, and you should be set! Load up `localhost:8080` on multiple browsers, sign up to your Auth0 system through the login button, and starting chatting!
