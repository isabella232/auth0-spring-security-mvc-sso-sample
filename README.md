## Using Auth0 with Spring Boot, Spring Security, and optionally AngularJS to do Single Sign On (SSO) between three websites

### Background

This folder contains three demo websites that have been developed to demonstrate the Auth0 SSO capabilities between a main "portal" website and two additional Partner websites that depends on the main "portal" site for SSO authentication. The third Site (Single Page Application written in Angular 1.x) is optional and provided for those wishing to do SSO with a mix of Server Side and Single Page apps.

### Contents

The included three demo websites are:

app1.com - main "portal" website - Java Server Side app - uses OIDC authorization code grant flow

app2.com - partner website - Java Server Side app

app3.com - partner website - Single Page App

The aim of this solution is to provide a simple, no-frills sample developers can follow to understand the orchestration
required to achieve SSO using Auth0 using Java, and optionally SPA app, without having to also cope with understanding additional libraries or frameworks.

#### Sequence Diagrams

* [New SSO Session Flow](sequence-diagrams/new-sso-session.md)
* [Existing SSO Session Flow](sequence-diagrams/existing-sso-session.md)

** Note the sequence diagrams are subject to change based on recent alterations to this sample.
However, they do provide a 10,000 foot overview.

### Setup

Create an [Auth0 Account](https://auth0.com) (if not already done so - free!).


#### From the Auth0 Dashboard

Create an application - for the purposes of this sample - `app1`
This will represent the Portal site

Ensure you add the following to the settings.

Allowed Callback URLs:

```
http://localhost:3099/callback
http://app1.com:3099/callback
```

Ensure you add the following to the settings.

Allowed Logout URLs:

```
http://localhost:3099/logout
http://app1.com:3099/logout
```

Ensure you switch ON the following setting.

`Use Auth0 instead of the IdP to do Single Sign On`



Create an application - for the purposes of this sample - `app2`
This will represent the Partner site

Ensure you add the following to the settings.

Allowed Callback URLs:

```
http://localhost:4000/callback
http://app2.com:4000/callback
```

Ensure you add the following to the settings.

Allowed Logout URLs:

```
http://localhost:4000/logout
http://app2.com:4000/logout
```

Ensure you switch ON the following setting.

`Use Auth0 instead of the IdP to do Single Sign On`



Create an application - for the purposes of this sample - `app3`
This will represent the Portal site (SPA)

Ensure you add the following to the settings.

Allowed Callback URLs:

```
http://localhost:3000
http://app3.com:3000
```

Ensure you switch ON the following setting.

`Use Auth0 instead of the IdP to do Single Sign On`



Define a `database connection` and ensure app1, app2 and app3 all reference this `connnection`

Go to `Users` and select `CREATE USER`

Create a user with a username / password of your choosing, and ensure the database connection defined above is
associated with that user.


Ok, that's all the Auth0 Dashboard setup done!


#### Update your hosts file

Update your `hosts` file so you can reference the apps by name, rather than localhost / 127.0.0.1

```
127.0.0.1    app1.com
127.0.0.1    app2.com
127.0.0.1    app3.com
```

Please see the individual README files in each app for further information specific to that application.

---

## License

The MIT License (MIT)

Copyright (c) 2016 AUTH10 LLC

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
