package com.vsantander.paymentchallenge.data.remote.utils

import com.vsantander.paymentchallenge.data.remote.model.ThumbnailTO

object AvatarUrlProvider {

    fun getAvatarFromThumbnail(thumbnail: ThumbnailTO): String {
        val path =  thumbnail.path.replace("\"", "")
        val extension = thumbnail.extension.replace("\"", "")
        return "$path.$extension"
    }

}