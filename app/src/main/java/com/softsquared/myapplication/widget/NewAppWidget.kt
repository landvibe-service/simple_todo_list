package com.softsquared.myapplication.widget

import android.app.Application
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.softsquared.myapplication.MainActivity
import com.softsquared.myapplication.MainViewModel
import com.softsquared.myapplication.R
import com.softsquared.myapplication.month.BaseCalendar
import com.softsquared.myapplication.month.MonthRecyclerAdapter
import kotlinx.android.synthetic.main.new_app_widget.*

class NewAppWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(
                context,
                appWidgetManager,
                appWidgetId
            )
        }
    }

    override fun onEnabled(context: Context) {
    }

    override fun onDisabled(context: Context) {
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val views = RemoteViews(context.packageName,
        R.layout.new_app_widget
    )
    val intent = Intent(context, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
    views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent)

    appWidgetManager.updateAppWidget(appWidgetId, views)



}