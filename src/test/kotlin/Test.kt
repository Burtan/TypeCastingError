import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.StringSpec
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import java.util.*

private object UuidStringSerializer: KSerializer<UUID> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): UUID =
        UUID.fromString(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: UUID) {
        encoder.encodeString(value.toString())
    }

}

private typealias Uuid = @Serializable(UuidStringSerializer::class) UUID

class CastTest : StringSpec() {
    init {
        "Cast is not useless" {
            val uuid: UUID = UUID.randomUUID()

            // succeeds, cast is NOT useless
            @Suppress("USELESS_CAST")
            Json.encodeToString(uuid as Uuid)

            // fails
            shouldThrowAny { Json.encodeToString(uuid) }
        }
    }
}

