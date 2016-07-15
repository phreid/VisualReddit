package com.preid.visualreddit;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class ImgurContentPresenter extends ContentPresenter {
    private static final String IMGUR_URL = "https://api.imgur.com/3";
    private static final String AUTH_HEADER = "Authorization";
    private static final String CLIENT_ID = "Client-ID 9bd2226c8db8e33";

    public ImgurContentPresenter(ContentZoomActivity parent) {
        super(parent);
    }

    @Override
    public void refreshContent(String url) {
        String id;
        String requestUrl;
        Class ResponseModel;

        if (LinkHandler.isImgurImage(url)) {
            id = LinkHandler.getImgurImageId(url);
            requestUrl = IMGUR_URL + "/image/" + id;
            ResponseModel = ImgurResponse.ImgurImageData.class;
        } else if (LinkHandler.isImgurAlbum(url)) {
            id = LinkHandler.getImgurAlbumId(url);
            requestUrl = IMGUR_URL + "/album/" + id + "/images";
            ResponseModel = ImgurResponse.ImgurAlbumData.class;
        } else {
            id = LinkHandler.getImgurGalleryId(url);
            requestUrl = IMGUR_URL + "/gallery/" + "image/" + id;
            ResponseModel = ImgurResponse.ImgurImageData.class;
        }

        Ion.with(getActivityParent())
                .load(requestUrl)
                .setHeader(AUTH_HEADER, CLIENT_ID)
                .as(ResponseModel)
                .setCallback(new FutureCallback<APIResponse>() {
                    @Override
                    public void onCompleted(Exception e, APIResponse result) {
                        getActivityParent().onContentLoaded(result.getContentData());
                    }
                });


    }
}
