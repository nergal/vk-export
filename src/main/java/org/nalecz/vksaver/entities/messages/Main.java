package org.nalecz.vksaver.entities.messages;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.ConversationWithMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.nalecz.vksaver.entities.AbstractEntity;

import java.util.ArrayList;
import java.util.List;

public class Main extends AbstractEntity {
    private static final Logger LOG = LogManager.getLogger(org.nalecz.vksaver.entities.photos.Main.class);

    public Main(DozerBeanMapper mapper, VkApiClient apiClient, UserActor actor, Integer userId) {
        super(mapper, apiClient, actor, userId);
    }

    public String proceed() throws ClientException, ApiException {
        List<ConversationWithMessage> getUserResponse = apiClient.messages()
                .getConversations(actor)
                .execute()
                .getItems();

        List<DialogObject> dialogObjects = new ArrayList<>();
        for (ConversationWithMessage dialog : getUserResponse) {
            LOG.warn(dialog);
        }

        return  "";
    }
}
