package com.softsquared.myapplication.widget

import android.content.Context
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import android.content.Intent



class GridRemoteViewsFactory : RemoteViewsService.RemoteViewsFactory {
    val mContext: Context
    constructor(context: Context, intent: Intent){
        mContext = context
    }
    override fun onCreate() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLoadingView(): RemoteViews {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemId(position: Int): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDataSetChanged() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hasStableIds(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getViewAt(position: Int): RemoteViews {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getViewTypeCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDestroy() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class GridWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsService.RemoteViewsFactory {
        // TODO Auto-generated method stub

        return GridRemoteViewsFactory(this.applicationContext, intent)
    }

}