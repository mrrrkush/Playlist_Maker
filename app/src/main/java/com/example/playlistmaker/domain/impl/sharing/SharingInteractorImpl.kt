package com.example.playlistmaker.domain.impl.sharing

import com.example.playlistmaker.data.sharing.ExternalNavigator
import com.example.playlistmaker.domain.api.sharing.SharingInteractor
import com.example.playlistmaker.domain.model.sharing.EmailData


class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
) : SharingInteractor {

    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return "https://practicum.yandex.ru/android-developer"
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            recipient = "mrklssh@yandex.ru",
            subject = "Сообщение разработчикам и разработчицам приложения Playlist Maker",
            body = "Спасибо разработчикам и разработчицам за крутое приложение!"
        )
    }

    private fun getTermsLink(): String {
        return "https://yandex.ru/legal/practicum_offer/"
    }

    override fun sharePlaylist(playlist: String) {
        externalNavigator.sharePlaylist(playlist)
    }
}
