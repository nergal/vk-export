package org.nalecz.vksaver;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;

import java.awt.Desktop;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;


public class Main {
    private static final String PROPERTIES_FILE = "config.properties";
    private static final Logger LOG = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        Properties properties = readProperties();
        initServer(properties);
    }

    private static void initServer(Properties properties) throws Exception {
        Integer port = Integer.valueOf(properties.getProperty("server.port"));
        String host = properties.getProperty("server.host");

        Integer clientId = Integer.valueOf(properties.getProperty("client.id"));
        String clientSecret = properties.getProperty("client.secret");

        HandlerCollection handlers = new HandlerCollection();

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setWelcomeFiles(new String[]{"index.html"});
        resourceHandler.setResourceBase(Main.class.getResource("/static").getPath());

        VkApiClient vk = new VkApiClient(new HttpTransportClient());
        handlers.setHandlers(new Handler[]{resourceHandler, new RequestHandler(vk, clientId, clientSecret, host)});

        Server server = new Server(port);
        server.setHandler(handlers);

        server.start();
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(new URI(host + "/login"));
        }

        server.join();
    }

    private static Properties readProperties() throws FileNotFoundException {
        InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE);
        if (inputStream == null) {
            throw new FileNotFoundException("property file '" + PROPERTIES_FILE + "' not found in the classpath");
        }

        try {
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException("Incorrect properties file");
        }
    }
}
