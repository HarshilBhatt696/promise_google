package my.promise.harshil.Model


import java.util.*


data class ImageMessage(val imagePath: String,
                        override val time: Date,
                        override val senderId: String,
                        override val recipientId: String,
                        override val senderName: String,
                        override val type: String = MessageType.IMAGE)
    : Mesage {
    constructor() : this("", Date(0), "", "", "")
}