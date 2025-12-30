package tmg.hourglass.room.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import tmg.hourglass.domain.repositories.CountdownRepository
import tmg.hourglass.domain.repositories.WidgetRepository
import tmg.hourglass.room.DATABASE_NAME
import tmg.hourglass.room.HourGlassDatabase
import tmg.hourglass.room.MIGRATION_1_2
import tmg.hourglass.room.backups.BackupManager
import tmg.hourglass.room.backups.BackupManagerImpl
import tmg.hourglass.room.dao.CountdownDao
import tmg.hourglass.room.dao.WidgetDao
import tmg.hourglass.room.repositories.CountdownRepositoryImpl
import tmg.hourglass.room.repositories.WidgetRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class RoomModule {

    @Provides
    fun provideBackupManager(impl: BackupManagerImpl): BackupManager = impl

    @Provides
    @Singleton
    fun provideHourGlassDatabase(
        @ApplicationContext
        applicationContext: Context
    ): HourGlassDatabase = Room
        .databaseBuilder(
            context = applicationContext,
            klass = HourGlassDatabase::class.java,
            name = DATABASE_NAME
        )
        .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
        .addMigrations(MIGRATION_1_2)
        .build()

    @Provides
    fun provideCountdownDao(
        database: HourGlassDatabase
    ): CountdownDao = database.countdownDao()

    @Provides
    fun provideWidgetDao(
        database: HourGlassDatabase
    ): WidgetDao = database.widgetDao()

    @Provides
    fun providesCountdownConnector(impl: CountdownRepositoryImpl): CountdownRepository = impl

    @Provides
    internal fun providesWidgetConnector(impl: WidgetRepositoryImpl): WidgetRepository = impl
}