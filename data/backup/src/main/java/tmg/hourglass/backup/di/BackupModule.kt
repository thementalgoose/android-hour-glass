package tmg.hourglass.backup.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.hourglass.backup.BackupManager
import tmg.hourglass.domain.repositories.CountdownRepository
import tmg.hourglass.domain.repositories.TagRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class BackupModule {

    @Provides
    @Singleton
    fun provideBackupManager(
        countdownRepository: CountdownRepository,
        tagRepository: TagRepository
    ): BackupManager {
        return BackupManager(
            countdownRepository = countdownRepository,
            tagRepository = tagRepository
        )
    }
}
