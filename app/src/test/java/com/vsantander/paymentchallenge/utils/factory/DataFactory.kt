package com.vsantander.paymentchallenge.utils.factory

import java.util.*

class DataFactory {

    companion object Factory {

        fun randomUuid(): String {
            return java.util.UUID.randomUUID().toString()
        }

        fun randomBoolean(): Boolean {
            return Random().nextBoolean()
        }

        fun randomInt(): Int {
            return Random().nextInt()
        }

        fun randomFloat(): Float {
            return Random().nextFloat()
        }

    }
}