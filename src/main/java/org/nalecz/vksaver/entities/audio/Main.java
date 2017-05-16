package org.nalecz.vksaver.entities.audio;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.audio.AudioFull;
import org.dozer.DozerBeanMapper;
import org.nalecz.vksaver.entities.AbstractEntity;

import java.util.ArrayList;
import java.util.List;

public class Main extends AbstractEntity {
    public Main(DozerBeanMapper mapper, VkApiClient apiClient, UserActor actor, Integer userId) {
        super(mapper, apiClient, actor, userId);
    }

    public String proceed() throws ClientException, ApiException {
        List<AudioFull> getUserResponse = apiClient.audio()
                .get(actor)
                .execute()
                .getItems();

        List<Audio> audios = new ArrayList<>();
        for (AudioFull audio : getUserResponse) {
            audios.add(mapper.map(audio, Audio.class));
        }

        return toJSON(audios);
    }
}
