package com.preid.visualreddit;

import java.util.ArrayList;
import java.util.List;

public class RedditResponse {
    private String kind;
    private ResponseData data;

    public RedditResponse() {}

    public List<Child> getChildren() {
        return data.getChildren();
    }

    public String getAfter() {
        if (data.getAfter() != null) {
            return data.getAfter();
        }

        return null;
    }

    public static class ResponseData {
        private List<Child> children;
        private String after;

        public List<Child> getChildren() {
            return children;
        }

        public String getAfter() {
            return after;
        }
    }

    public static class Child {
        private String kind;
        private RedditPostData data;

        public String getKind() {
            return kind;
        }

        public RedditPostData getData() {
            return data;
        }
    }

    public static class RedditPostData implements APIResponse<String> {

        private String thumbnail;
        private PreviewData preview;
        private String url;
        private String title;

        public String getThumbnail() {
            return thumbnail;
        }

        public String getUrl() {
            return url;
        }

        public String getTitle() {
            return title;
        }

        public PreviewData getPreview() {
            return preview;
        }

        public String getSource() {
            return preview.getImages().get(0).getSource().getUrl();
        }

        public String getLowRes() {
            if (preview != null) {
                List<SourceData> resolutions = preview.getImages().get(0).getResolutions();

                if (resolutions.size() > 1) {
                    return resolutions.get(1).getUrl();
                }
            }

            return thumbnail;
        }

        @Override
        public List<String> getContentData() {
            List<String> urlList = new ArrayList<>();

            urlList.add(getUrl());

            return urlList;
        }

        public static class PreviewData {
            private List<ImageData> images;

            public List<ImageData> getImages() {
                return images;
            }
        }

        public static class ImageData {
            private SourceData source;
            private List<SourceData> resolutions;

            public SourceData getSource() {
                return source;
            }

            public List<SourceData> getResolutions() {
                return resolutions;
            }
        }

        public static class SourceData {
            private String url;

            public String getUrl() {
                return url;
            }
        }
    }
}
