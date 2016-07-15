package com.preid.visualreddit;

import java.util.List;

public interface APIResponse<T> {
    List<T> getContentData();
}
