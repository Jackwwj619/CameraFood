package com.caloriesnap.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.caloriesnap.R
import com.caloriesnap.data.local.AppDatabase
import com.caloriesnap.domain.usecase.CalorieCalculator
import kotlinx.coroutines.*
import java.time.LocalDate

class CalorieWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        appWidgetIds.forEach { updateWidget(context, appWidgetManager, it) }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == "com.caloriesnap.UPDATE_WIDGET") {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val ids = appWidgetManager.getAppWidgetIds(android.content.ComponentName(context, CalorieWidget::class.java))
            onUpdate(context, appWidgetManager, ids)
        }
    }

    private fun updateWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val db = AppDatabase.getInstance(context)
                val today = LocalDate.now().toString()
                val records = db.foodRecordDao().getByDateSync(today)
                val consumed = records.sumOf { r -> r.calories.toDouble() }.toFloat()

                val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
                val target = prefs.getFloat("widget_target", 2000f)

                val views = RemoteViews(context.packageName, R.layout.widget_calorie)
                views.setTextViewText(R.id.widget_calories, "${consumed.toInt()} / ${target.toInt()} kcal")
                val progress = ((consumed / target) * 100).coerceIn(0f, 100f).toInt()
                views.setProgressBar(R.id.widget_progress, 100, progress, false)
                val remaining = (target - consumed).coerceAtLeast(0f).toInt()
                views.setTextViewText(R.id.widget_remaining, "剩余 $remaining kcal")

                val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
                val pendingIntent = android.app.PendingIntent.getActivity(context, 0, intent, android.app.PendingIntent.FLAG_IMMUTABLE)
                views.setOnClickPendingIntent(R.id.widget_calories, pendingIntent)

                withContext(Dispatchers.Main) {
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
            } catch (_: Exception) {}
        }
    }
}
