package org.nalecz.vksaver;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.objects.UserAuthResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RequestHandler extends AbstractHandler {

    private static final Logger LOG = LogManager.getLogger(RequestHandler.class);

    private final String clientSecret;
    private final int clientId;
    private final String host;
    private final VkApiClient vk;

    private String token;
    private int user;

    RequestHandler(VkApiClient vk, int clientId, String clientSecret, String host) {
        this.vk = vk;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.host = host;
    }

    private void proceedEntries() {
        UserActor actor = new UserActor(user, token);

        EntityManager entityManager = EntityManager.getInstance(vk, user, token);
        entityManager.processRequests(new int[]{EntityManager.ENTITY_FRIENDS}, actor);
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        switch (target) {
            case "/info":
                try {
                    user = Integer.parseInt(baseRequest.getParameter("user"));
                    token = baseRequest.getParameter("token");

                    response.setContentType("text/html;charset=utf-8");
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().println("<script type=\"text/javascript\">window.top.close();</script>");
                } catch (Exception e) {
                    response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
                    response.getWriter().println("error");
                    response.setContentType("text/html;charset=utf-8");
                    e.printStackTrace();
                }

                baseRequest.setHandled(true);
                new Thread(() -> {
                    try {
                        getServer().stop();
                        proceedEntries();
                    } catch (Exception ex) {
                        LOG.info("Failed to stop Jetty", ex);
                    }
                }).start();

                break;

            case "/callback":
                try {
                    UserAuthResponse authResponse = vk.oauth().userAuthorizationCodeFlow(clientId, clientSecret, getRedirectUri(), baseRequest.getParameter("code")).execute();
                    response.sendRedirect("/info?token=" + authResponse.getAccessToken() + "&user=" + authResponse.getUserId());
                } catch (Exception e) {
                    response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
                    response.getWriter().println("error");
                    response.setContentType("text/html;charset=utf-8");
                    e.printStackTrace();
                }

                baseRequest.setHandled(true);
                break;

            case "/login":
                response.sendRedirect(getOAuthUrl());
                baseRequest.setHandled(true);
                break;
        }
    }

    private String[] getPermissionsList() {
        return new String[]{
//                "messages",
                "groups",
                "photos",
                "friends",
                "audio",
                "video",
                "notes",
                "pages",
                "wall",
                "docs"
        };
    }

    private String getOAuthUrl() {
        // @TODO get permissions from module
        return String.format(
                "https://oauth.vk.com/authorize?client_id=%s&display=page&redirect_uri=%s&scope=%s&response_type=code",
                clientId,
                getRedirectUri(),
                String.join(",", getPermissionsList())
        );
//        return "https://oauth.vk.com/authorize?client_id=" + clientId + "&display=page&redirect_uri=" + getRedirectUri() + "&scope=" + String.join(",", getPermissionsList()) + "&response_type=code";
    }

    private String getRedirectUri() {
//        return "http://oauth.vk.com/blank.html";
         return host + "/callback";
    }
}
