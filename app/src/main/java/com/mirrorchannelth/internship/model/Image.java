package com.mirrorchannelth.internship.model;

import android.net.Uri;

/**
 * Created by boss on 5/22/16.
 */
public class Image {

        private String id = null;
        private String url = null;
        private String protocol = null;
        private Uri uri;

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public Image(String url) {
            this.setUrl(url);
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getProtocol() {
            return protocol;
        }

        public void setProtocol(String protocol) {
            this.protocol = protocol;
        }
}
