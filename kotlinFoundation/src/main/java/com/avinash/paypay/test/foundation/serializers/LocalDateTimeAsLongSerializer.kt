package com.avinash.paypay.test.foundation.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Custom Serializer class for [LocalDateTime]
 */
object LocalDateTimeAsLongSerializer : KSerializer<LocalDateTime> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: LocalDateTime) = encoder.encodeLong(value.timeInSeconds)

    override fun deserialize(decoder: Decoder): LocalDateTime =
        LocalDateTime.ofInstant(Instant.ofEpochSecond(decoder.decodeLong()), ZoneId.systemDefault())
}

/**
 * Returns number of seconds from the epoch based on systemDefault TimeZone.
 * @return number of seconds from the epoch
 */
val LocalDateTime.timeInSeconds: Long
    get() {
        return this.atZone(ZoneId.systemDefault()).toEpochSecond()
    }
