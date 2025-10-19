package tmg.hourglass.room.repositories

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import tmg.hourglass.domain.model.WidgetReference
import tmg.hourglass.domain.repositories.WidgetRepository
import tmg.hourglass.room.dao.WidgetDao
import tmg.hourglass.room.mappers.WidgetMapper
import javax.inject.Inject

internal class WidgetRepositoryImpl @Inject constructor(
    private val widgetDao: WidgetDao,
    private val widgetMapper: WidgetMapper,
): WidgetRepository {
    override fun saveSync(widgetReference: WidgetReference) {
        Log.d("Room", "Saving $widgetReference")
        val model = widgetMapper.serialize(widgetReference)
        runBlocking { widgetDao.insertWidgetRef(model) }
    }

    override fun getSync(appWidgetId: Int): WidgetReference? {
        val model = runBlocking { widgetDao.getWidgetRef(appWidgetId).firstOrNull() } ?: return null
        return widgetMapper.deserialize(model)
    }

    override fun get(appWidgetId: Int): Flow<WidgetReference?> {
        return widgetDao.getWidgetRef(appWidgetId)
            .map {
                it?.let { widgetMapper.deserialize(it) }
            }
    }
}