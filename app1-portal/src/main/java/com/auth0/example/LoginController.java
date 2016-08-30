package com.auth0.example;

import com.auth0.Auth0User;
import com.auth0.NonceUtils;
import com.auth0.QueryParamUtils;
import com.auth0.SessionUtils;
import com.auth0.spring.security.mvc.Auth0Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class LoginController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Auth0Config auth0Config;
    private SsoConfig ssoConfig;

    @Autowired
    public LoginController(Auth0Config auth0Config, SsoConfig ssoConfig) {
        this.auth0Config = auth0Config;
        this.ssoConfig = ssoConfig;
    }

    @RequestMapping(value="/login", method = RequestMethod.GET)
    protected String login(final Map<String, Object> model, final HttpServletRequest req) {
        logger.debug("Performing login");
        detectError(model);
        // add Nonce to storage
        NonceUtils.addNonceToStorage(req);
        removeExternalReturnUrlFromState(req);
        setupModel(model, req);
        // for this sample only, this just allows configuration to
        // use Lock Widget or Auth0.js for login presentation
        return ssoConfig.isCustomLogin() ? "loginCustom" : "login";
    }

    private void removeExternalReturnUrlFromState(HttpServletRequest req) {
        // explicitly remove any externalReturnUrl if in state storage - this is the Portal login
        // we want to ensure no implicit redirection to Partner site is possible post authentication
        final String previousState = SessionUtils.getState(req);
        final String updatedState = QueryParamUtils.removeFromQueryParams(previousState, "externalReturnUrl");
        SessionUtils.setState(req, updatedState);
    }

    /**
     * required attributes needed in request for view presentation
     */
    protected void setupModel(final Map<String, Object> model, final HttpServletRequest req) {
        // null if no user exists..
        final Auth0User user = SessionUtils.getAuth0User(req);
        model.put("user", user);
        model.put("domain", auth0Config.getDomain());
        model.put("clientId", auth0Config.getClientId());
        model.put("loginCallback", auth0Config.getLoginCallback());
        model.put("loginRedirectOnSuccess", auth0Config.getLoginRedirectOnSuccess());
        model.put("loginRedirectOnFail", auth0Config.getLoginRedirectOnFail());
        model.put("state", SessionUtils.getState(req));
        // sso config specific
        model.put("logoutEndpoint", ssoConfig.getLogoutEndpoint());
        model.put("connection", ssoConfig.getConnection());
    }

    protected void detectError(final Map<String, Object> model) {
        if (model.get("error") != null) {
            model.put("error", true);
        } else {
            model.put("error", false);
        }
    }

}
