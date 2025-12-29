package com.example.clockapp.di

import android.content.Context
import com.example.clockapp.utils.AlarmPlayer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import dagger.hilt.components.SingletonComponent

import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

// i create that to be be my tool box that contain  the shared objects that we need in multiple places.
@Module
@InstallIn(SingletonComponent::class)
object MapModule {

 @Provides
 @Singleton
 // i used that for track location instead of put every time in viewmodel
 fun provideFusedLocationProviderClient (
   @ApplicationContext context: Context
 ):FusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(context)


  // Provide IO dispatcher for background work
  @Provides
  @Singleton
  fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

 @Provides
 @Singleton
 fun provideAlarmPlayer(
  @ApplicationContext context: Context
 ): AlarmPlayer =AlarmPlayer(context)


}