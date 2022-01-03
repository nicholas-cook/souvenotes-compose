package com.souvenotes.souvenotes

import android.app.Application
import android.content.res.AssetManager
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.souvenotes.repository.notes.NotesRepository
import com.souvenotes.repository.notes.NotesRepositoryImpl
import com.souvenotes.repository.user.UserRepository
import com.souvenotes.repository.user.UserRepositoryImpl
import com.souvenotes.souvenotes.landing.LandingViewModel
import com.souvenotes.souvenotes.login.LoginViewModel
import com.souvenotes.souvenotes.login.forgotpassword.ForgotPasswordViewModel
import com.souvenotes.souvenotes.notes.NotesListViewModel
import com.souvenotes.souvenotes.notes.edit.EditNoteViewModel
import com.souvenotes.souvenotes.policies.PolicyViewModel
import com.souvenotes.souvenotes.registration.RegistrationViewModel
import com.souvenotes.souvenotes.repository.prefs.SouvenotesPrefs
import com.souvenotes.souvenotes.repository.prefs.SouvenotesPrefsImpl
import com.souvenotes.souvenotes.settings.SettingsViewModel
import com.souvenotes.souvenotes.settings.delete.DeleteAccountViewModel
import com.souvenotes.souvenotes.settings.email.ChangeEmailViewModel
import com.souvenotes.souvenotes.settings.password.ChangePasswordViewModel
import com.souvenotes.souvenotes.settings.reauth.ReauthViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class SouvenotesApplication : Application() {

    private val appModule = module {
        factory<AssetManager> { get<Application>().assets }
        single<UserRepository> { UserRepositoryImpl() }
        single<NotesRepository> { NotesRepositoryImpl(get()) }
        single<SouvenotesPrefs> { SouvenotesPrefsImpl(get()) }
        viewModel<LandingViewModel>()
        viewModel<RegistrationViewModel>()
        viewModel<ForgotPasswordViewModel>()
        viewModel<LoginViewModel>()
        viewModel { parameters -> PolicyViewModel(parameters.get(), get()) }
        viewModel<NotesListViewModel>()
        viewModel { parameters -> EditNoteViewModel(parameters.get(), parameters.get(), get()) }
        viewModel<SettingsViewModel>()
        viewModel<ReauthViewModel>()
        viewModel<ChangeEmailViewModel>()
        viewModel<ChangePasswordViewModel>()
        viewModel<DeleteAccountViewModel>()
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@SouvenotesApplication)
            modules(appModule)
        }
        FirebaseApp.initializeApp(this)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        MobileAds.initialize(this)
        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
                .setTestDeviceIds(listOf("B4894F1DEA524953E7AE0E7B249B9776")).build()
        )
    }
}