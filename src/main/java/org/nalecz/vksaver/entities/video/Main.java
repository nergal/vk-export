package org.nalecz.vksaver.entities.video;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.nalecz.vksaver.entities.AbstractEntity;

import java.util.ArrayList;
import java.util.List;

public class Main extends AbstractEntity {
    private static final Logger LOG = LogManager.getLogger(Main.class);

    public Main(DozerBeanMapper mapper, VkApiClient apiClient, UserActor actor, Integer userId) {
        super(mapper, apiClient, actor, userId);
    }

    public String proceed() throws ClientException, ApiException {
        List<com.vk.api.sdk.objects.video.Video> getUserResponse = apiClient.videos()
                .get(actor)
                .execute()
                .getItems();

        List<org.nalecz.vksaver.entities.video.Video> videos = new ArrayList<>();
        for (com.vk.api.sdk.objects.video.Video video : getUserResponse) {
            // @TODO get comments also
            videos.add(mapper.map(video, org.nalecz.vksaver.entities.video.Video.class));
        }

        return toJSON(videos);
    }
}
