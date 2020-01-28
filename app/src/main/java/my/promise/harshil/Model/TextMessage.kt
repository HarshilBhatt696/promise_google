package my.promise.harshil.Model

import java.util.*

data class TextMessage(val text: String,
                       override val time: Date,
                       override val senderId: String,
                       override val recipientId: String,
                       override val senderName: String,
                       override val type: String = MessageType.TEXT)
    : Mesage {
    constructor() : this("", Date(0), "", "", "")
}