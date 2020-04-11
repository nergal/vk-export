package org.nalecz.vksaver.entities.wall;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.wall.WallpostFull;
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
        List<WallpostFull> getUserResponse = apiClient.wall()
                .get(actor)
                .execute()
                .getItems();

        List<WallObject> wallObjects = new ArrayList<>();
        for (WallpostFull wallPost : getUserResponse) {
            LOG.warn(wallPost);
        }

        return  "";
    }
}
