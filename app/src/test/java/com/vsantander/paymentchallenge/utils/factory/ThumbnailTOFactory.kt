package com.vsantander.paymentchallenge.utils.factory

import com.vsantander.paymentchallenge.data.remote.model.ThumbnailTO

/**
 * Factory class for ThumbnailTO related instances
 */
class ThumbnailTOFactory {

    companion object {

        fun makeThumbnailTOModel(): ThumbnailTO {
            return ThumbnailTO(
                    path = DataFactory.randomUuid(),
                    extension = DataFactory.randomUuid()
            )
        }
    }
}