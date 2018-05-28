package com.devmarcul.maevent.utils.network;

public class NetworkUtils {

    public static class UrlBuilder
    {
        private StringBuilder mBuilder;

        public UrlBuilder() {
            mBuilder = new StringBuilder();
        }

        public UrlBuilder setBase(String base)
        {
            mBuilder.setLength(0);
            mBuilder.append(base);
            return this;
        }

        public String build(String container)
        {
            return mBuilder.append(container).toString();
        }
    }
}
