package com.auth0.example;

import com.auth0.Auth0User;
import com.auth0.NonceUtils;
import com.auth0.QueryParamUtils;
import com.auth0.SessionUtils;
import com.auth0.spring.security.mvc.Auth0Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Controller
public class PartnerLoginController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Auth0Config auth0Config;
    private SsoConfig ssoConfig;

    @Autowired
    public PartnerLoginController(Auth0Config auth0Config, SsoConfig ssoConfig) {
        this.auth0Config = auth0Config;
        this.ssoConfig = ssoConfig;
    }

    @RequestMapping(value="/partner", method = RequestMethod.GET)
    protected String partnerLogin(final Map<String, Object> model, final HttpServletRequest req) {
        logger.debug("Performing partner login");
        detectError(model);
        manageExternalReturnUrl(req);
        // add Nonce to storage
        NonceUtils.addNonceToStorage(req);
        setupModel(model, req);
        // for this sample only, this just allows configuration to
        // use Lock Widget or Auth0.js for login presentation
        return ssoConfig.isCustomLogin() ? "partnerLoginCustom" : "partnerLogin";
    }

    private void manageExternalReturnUrl(HttpServletRequest req) {
        final String externalReturnUrl = req.getParameter("externalReturnUrl");
        if (externalReturnUrl == null) {
            throw new IllegalArgumentException("Missing required external return URL query param.");
        } else  if (!isTrustedExternalReturnUrl(externalReturnUrl)) {
            throw new IllegalArgumentException("Cannot redirect to untrusted URL: " + externalReturnUrl);
        }
        final String previousState = (SessionUtils.getState(req) != null) ? SessionUtils.getState(req) : "";
        final String updatedState = QueryParamUtils.addOrReplaceInQueryParams(previousState, "externalReturnUrl", externalReturnUrl);
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
        model.put("state", SessionUtils.getState(req));
        // sso config specific
        model.put("connection", ssoConfig.getConnection());
    }

    protected void detectError(final Map<String, Object> model) {
        if (model.get("error") != null) {
            model.put("error", true);
        } else {
            model.put("error", false);
        }
    }

    protected boolean isTrustedExternalReturnUrl(final String url) {
        return ssoConfig.getTrustedExternalReturnUrls().contains(url);
    }

    /**
     * We want to treat IllegalArgumentException as a 400 error in this particular situation
     */
    @ExceptionHandler
    void handleIllegalArgumentException(final IllegalArgumentException e, final HttpServletResponse res) throws IOException {
        logger.info(e.getLocalizedMessage());
        res.sendError(HttpStatus.BAD_REQUEST.value());
    }


}
