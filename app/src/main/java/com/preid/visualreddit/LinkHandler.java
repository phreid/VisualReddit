package com.preid.visualreddit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkHandler {
    public static final Pattern
        imgurPattern = Pattern.compile(".*[^A-Za-z]imgur\\.com/(\\w+).*"),
        imgurAlbumPattern = Pattern.compile(".*[^A-Za-z]imgur\\.com/(a)/(\\w+).*"),
        imgurGalleryPattern = Pattern.compile(".*[^A-Za-z]imgur\\.com/(gallery)/(\\w+).*"),
        redditImagePattern = Pattern.compile(".*i\\.redd.*/(\\w+).*");

    public static boolean canHandle(String url) {
        return isImgurLink(url) || isImgurGallery(url) || isImgurAlbum(url) || isRedditImage(url);
    }

    public static void handle(ContentZoomActivity activity, String url) {
        ContentPresenter presenter;

        if (isImgurLink(url)) {
            presenter = new ImgurContentPresenter(activity);
            presenter.refreshContent(url);
        } else {
            presenter = new ContentPresenter(activity);
            presenter.refreshContent(url);
        }
    }

    public static boolean isRedditImage(String url) {
        Matcher matcher = redditImagePattern.matcher(url);

        return matcher.find();
    }

    public static boolean isImgurLink(String url) {
        return (isImgurImage(url) || isImgurAlbum(url) || isImgurGallery(url));
    }

    public static boolean isImgurAlbum(String url) {
        Matcher matcher = imgurAlbumPattern.matcher(url);

        return matcher.find();
    }

    public static boolean isImgurGallery(String url) {
        Matcher matcher = imgurGalleryPattern.matcher(url);

        return matcher.find();
    }

    public static boolean isImgurImage(String url) {
        Matcher matcher = imgurPattern.matcher(url);

        if (matcher.find()) {
            String imageId = matcher.group(1);
            if (! (imageId.startsWith("gallery") || imageId.startsWith("/a/"))) {
                return true;
            }
        }

        return false;
    }

    public static String getImgurAlbumId(String url) {
        Matcher matcher = imgurAlbumPattern.matcher(url);

        if (matcher.find()) {
            return matcher.group(2);
        }

        return null;
    }

    public static String getImgurGalleryId(String url) {
        Matcher matcher = imgurGalleryPattern.matcher(url);

        if (matcher.find()) {
            return matcher.group(2);
        }

        return null;
    }

    public static String getImgurImageId(String url) {
        Matcher matcher = imgurPattern.matcher(url);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    public static boolean isImgurVideo(String url) {
        Matcher matcher = imgurPattern.matcher(url);

        if (matcher.find()) {
            return (url.contains(".gifv") || url.contains(".webm") || url.contains(".mp4"));
        }

        return false;
    }
}
