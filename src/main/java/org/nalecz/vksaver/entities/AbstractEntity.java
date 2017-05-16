package org.nalecz.vksaver.entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import org.dozer.DozerBeanMapper;

import java.io.IOException;

public abstract class AbstractEntity {
    protected DozerBeanMapper mapper;

    protected VkApiClient apiClient;
    protected UserActor actor;
    protected int userId;

    protected AbstractEntity(DozerBeanMapper mapper, VkApiClient apiClient, UserActor actor, Integer userId) {
        this.mapper = mapper;
        this.apiClient = apiClient;
        this.actor = actor;
        this.userId = userId;
    }

    protected String toJSON(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        String result = null;

        try {
            result = mapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(object);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
