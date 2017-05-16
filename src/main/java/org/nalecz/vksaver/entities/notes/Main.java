package org.nalecz.vksaver.entities.notes;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.notes.Note;
import org.dozer.DozerBeanMapper;
import org.nalecz.vksaver.entities.AbstractEntity;

import java.util.ArrayList;
import java.util.List;

public class Main extends AbstractEntity {
    public Main(DozerBeanMapper mapper, VkApiClient apiClient, UserActor actor, Integer userId) {
        super(mapper, apiClient, actor, userId);
    }

    public String proceed() throws ClientException, ApiException {
        List<Note> getUserResponse = apiClient.notes()
                .get(actor)
                .execute()
                .getItems();

        List<NoteObject> notes = new ArrayList<>();
        for (Note note : getUserResponse) {
            // @TODO get comments also
            notes.add(mapper.map(note, NoteObject.class));
        }

        return toJSON(notes);
    }
}
