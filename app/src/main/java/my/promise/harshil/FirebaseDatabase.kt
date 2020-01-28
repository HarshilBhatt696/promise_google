package my.promise.harshil


import my.promise.harshil.Util.FireStoreUtil


import com.google.firebase.auth.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError


import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


val FireabaseClass = FirebaseDatabaseClass()
class FirebaseDatabaseClass() {

    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference()
    val Users = arrayListOf<String>()
    var UserName = ""
    var Gender = ""

//    fun AssignDescription() {
//
//        for (n in UserInfo.Interest) {
//
//            myRef.child(References.UserDetail.toString()).child(Gender).child(UserName).child("About").child(n.key).setValue(UserInfo.Interest[n.key])
//
//        }
//
//
//    }




    fun AssignInterest() {

        val Interest = Interests.Category
        val InterestGathered = Interests.CategoryGatheredTypes


        val user = FireabaseClass
        val UserName = FireabaseClass.mAuth.currentUser!!.displayName.toString()
            for (n in Interest.indices) {


                if (FinalUsersName == "0000000") {
                    myRef.child(References.Likes.toString()).child(Interest[n]).child(InterestGathered[n])
                        .child(user.Gender).child(
                        FinalUsersName
                    ).setValue("1")
                } else {
                    myRef.child(References.Likes.toString()).child(Interest[n]).child(InterestGathered[n])
                        .child(user.Gender).child(
                            UserName
                        ).setValue("1")
                }
            }






    }


    fun IfUserExists() {

        if (FireabaseClass.mAuth.currentUser!!.displayName != null) {

            FireabaseClass.mAuth.currentUser!!.displayName





        } else {




        }


    }












}



