package com.apps.newstudio.tasklist.utils;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.apps.newstudio.tasklist.data.adapters.WidgetFactory;

public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetFactory(TaskListApplication.getContext(), intent);
    }
}
