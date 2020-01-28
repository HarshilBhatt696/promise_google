package my.promise.harshil.Model

import java.util.*


object MessageType {
    const val TEXT = "TEXT"
    const val IMAGE = "IMAGE"
}


interface Mesage {

    val time: Date
    val senderId: String
    val recipientId: String
    val senderName: String
    val type: String

}