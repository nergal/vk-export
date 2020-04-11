package org.nalecz.vksaver.entities.friends;

import com.google.common.base.Functions;
import com.google.common.collect.Lists;
import com.vk.api.sdk.client.Lang;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.friends.responses.GetResponse;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import org.dozer.DozerBeanMapper;
import org.nalecz.vksaver.entities.AbstractEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main extends AbstractEntity {
    public Main(DozerBeanMapper mapper, VkApiClient apiClient, UserActor actor, Integer userId) {
        super(mapper, apiClient, actor, userId);
    }

    private List<Fields> getFields() {
        return new ArrayList<>() {{
            add(Fields.BDATE);
            add(Fields.CITY);
            add(Fields.COUNTRY);
            add(Fields.EXPORTS);
            add(Fields.PHOTO_MAX_ORIG);
            add(Fields.SCREEN_NAME);
            add(Fields.NICKNAME);
            add(Fields.SITE);
        }};
    }

    public String proceed() throws ClientException, ApiException {
        GetResponse friendsResponse = apiClient.friends()
                .get(actor)
                .userId(userId)
                .execute();

        List<Fields> friendFields = getFields();
        List<String> friendsIds = friendsResponse
                .getItems()
                .stream()
                .map(Functions.toStringFunction()::apply)
                .collect(Collectors.toList());

        List<UserXtrCounters> getUsersResponse = apiClient.users()
                .get(actor)
                .userIds(friendsIds)
                .fields(friendFields)
                .lang(Lang.RU)
                .execute();

        List<Friend> friends = new ArrayList<>();
        for (UserXtrCounters user : getUsersResponse) {
            friends.add(mapper.map(user, Friend.class));
        }

        return toJSON(friends);
    }
}
