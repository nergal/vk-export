package org.nalecz.vksaver;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.nalecz.vksaver.entities.wall.Main;

import java.util.ArrayList;
import java.util.List;

class EntityManager {
    static final int ENTITY_FRIENDS = 1;
    // WALL
    // PHOTOS
    // LIKES

    private static final Logger LOG = LogManager.getLogger(EntityManager.class);

    private String token;
    private int user;
    private VkApiClient apiClient;

    private DozerBeanMapper mapper;

    private static EntityManager entiyManager;

    static EntityManager getInstance(VkApiClient apiClient, int user, String token) {
        if (entiyManager == null) {
            entiyManager = new EntityManager(apiClient, user, token);
        }

        return entiyManager;
    }

    private EntityManager(VkApiClient apiClient, int user, String token) {
        this.apiClient = apiClient;
        this.user = user;
        this.token = token;

        List<String> mapperFiles = new ArrayList<>();
        mapperFiles.add("mapping.xml");

        mapper = new DozerBeanMapper();
        mapper.setMappingFiles(mapperFiles);
    }

    void processRequests(int[] entities, UserActor actor) {
        String output = "";

        try {
            Main friends = new Main(mapper, apiClient, actor, user);
            output = friends.proceed();
        } catch (ApiException | ClientException e) {
            LOG.error(e.getLocalizedMessage());
        } finally {
            System.out.println(output);
        }
    }
}
