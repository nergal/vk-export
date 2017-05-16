package org.nalecz.vksaver.entities.friends;

import com.google.common.base.Functions;
import com.google.common.collect.Lists;
import com.vk.api.sdk.client.Lang;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.friends.responses.GetResponse;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.queries.users.UserField;
import org.dozer.DozerBeanMapper;
import org.nalecz.vksaver.entities.AbstractEntity;

import java.util.ArrayList;
import java.util.List;

public class Main extends AbstractEntity {
    public Main(DozerBeanMapper mapper, VkApiClient apiClient, UserActor actor, Integer userId) {
        super(mapper, apiClient, actor, userId);
    }

    private List<UserField> getFields() {
        return new ArrayList<UserField>(){{
            add(UserField.BDATE);
            add(UserField.CITY);
            add(UserField.COUNTRY);
            add(UserField.EXPORTS);
            add(UserField.PHOTO_MAX_ORIG);
            add(UserField.SCREEN_NAME);
            add(UserField.NICKNAME);
            add(UserField.SITE);
        }};
    }

    public String proceed() throws ClientException, ApiException {
        GetResponse friendsResponse = apiClient.friends()
                .get(actor)
                .userId(userId)
                .execute();

        List<UserField> friendFields = getFields();
        List<String> friendsIds = Lists.transform(friendsResponse.getItems(), Functions.toStringFunction());

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
