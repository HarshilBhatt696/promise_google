package my.promise.harshil.Model

data class ChatChannel(val userIds: MutableList<String>) {
    constructor() : this(mutableListOf())
}