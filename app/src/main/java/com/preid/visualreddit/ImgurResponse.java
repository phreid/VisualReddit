package com.preid.visualreddit;

import java.util.ArrayList;
import java.util.List;

public class ImgurResponse {

    public static class ImgurImageData implements APIResponse<String> {
        private Data data;

        public static class Data {
            private String id;
            private String link;
            private String gifv;
            private String webm;
        }

        public String getId() {
            return data.id;
        }

        public String getLink() {
            return data.link;
        }

        public String getGifv() {
            return data.gifv;
        }

        public String getWebm() {
            return data.webm;
        }

        public List<String> getContentData() {
            List<String> urlList = new ArrayList<>();

            if (data.webm != null) {
                urlList.add(data.webm);
            } else {
                urlList.add(data.link);
            }

            return urlList;
        }
    }

    public static class ImgurAlbumData implements APIResponse<String> {
        private List<ImgurAlbumImageData> data;
        private boolean success;

        public static class ImgurAlbumImageData {
            private String link;

            public String getLink() {
                return link;
            }
        }

        public List<String> getContentData() {
            List<String> imageUrls = new ArrayList<>();

            for (int i = 0; i < data.size(); i++) {
                ImgurAlbumImageData imageData = data.get(i);
                imageUrls.add(imageData.getLink());
            }

            return imageUrls;
        }
    }
}