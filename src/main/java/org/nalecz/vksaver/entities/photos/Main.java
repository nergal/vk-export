package org.nalecz.vksaver.entities.photos;

import com.google.common.collect.Iterables;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.photos.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.nalecz.vksaver.entities.AbstractEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main extends AbstractEntity {
    private static final Logger LOG = LogManager.getLogger(Main.class);


    private static final int PHOTO_TIMEOUT = 1000;
    private static final int PHOTO_LIMIT = 200;

    public Main(DozerBeanMapper mapper, VkApiClient apiClient, UserActor actor, Integer userId) {
        super(mapper, apiClient, actor, userId);
    }

    private String findBestQuality(List<PhotoSizes> photoSizes) {
        String value = null;

        if (photoSizes != null) {
            PhotoSizes photoSize = Iterables.getLast(photoSizes, null);
            if (photoSize != null) {
                value = photoSize.getUrl().toString();
            }
        }

        return value;
    }

    public String proceed() throws ClientException, ApiException, InterruptedException {
        List<PhotoAlbumFull> albumItems = apiClient.photos()
                .getAlbums(actor)
                .ownerId(userId)
                .execute()
                .getItems();

        List<PhotoXtrRealOffset> photoItems = new ArrayList<>();

        List<PhotoXtrRealOffset> partialPhotoItems;
        do {
            Thread.sleep(PHOTO_TIMEOUT);

            partialPhotoItems = apiClient.photos()
                    .getAll(actor)
                    .photoSizes(true)
                    .offset(photoItems.size())
                    .count(PHOTO_LIMIT)
                    .ownerId(userId)
                    .execute()
                    .getItems();

            photoItems.addAll(partialPhotoItems);
        } while (partialPhotoItems.size() >= PHOTO_LIMIT);


        List<AlbumObject> albums = new ArrayList<>();
        for (PhotoAlbumFull album : albumItems) {
            List<PhotoObject> photos = new ArrayList<>();
            for (PhotoXtrRealOffset photo : photoItems) {
                if (Objects.equals(photo.getAlbumId(), album.getId())) {
                    PhotoObject photoObject = mapper.map(photo, PhotoObject.class);
                    photoObject.setSrc(findBestQuality(photo.getSizes()));

                    // @TODO download src
                    // @TODO grab comments
                    // @TODO add workaround for setSrc in the object above
                    photos.add(photoObject);
                }
            }

            AlbumObject albumObject = mapper.map(album, AlbumObject.class);
            albumObject.setPhotos(photos);

            albums.add(albumObject);
        }

        return toJSON(albums);
    }
}