package my.promise.harshil.Model

data class User(val name : String , val gender : String , val bio : String , val profilePicturePath : String? , val interest : ArrayList<String> , val registrationTokens: MutableList<String>, val FireabseAuthToken : String , val numberOfPeopleDating : ArrayList<String> , val requests : ArrayList<String> ,val declineOrBlocked  : ArrayList<String> , val notToBeViwedAnywhere : ArrayList<String> , val matchMakeSameGender : Boolean, val messages: MutableMap<String, Int> , val todaysMatchMakes : ArrayList<String>, val sentRequestsForToday : Int , val timeSentRequests : String ) {
    constructor() : this("" , "" , "", null  ,ArrayList<String>() , mutableListOf() , "" , ArrayList<String>() , ArrayList<String>() ,ArrayList<String>() , ArrayList<String>() , true  ,   mutableMapOf()   ,  ArrayList<String>() , 0  , "")


}

