package com.devmarcul.maevent.common;

public interface ContentAdapter<T, VH> {
    void adaptContent(T obj);
    void bindOnClickListeners();
    void unbindOnClickListeners();
    VH getViewHolder();
}
