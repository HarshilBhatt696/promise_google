package my.promise.harshil

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import my.promise.harshil.Util.FireStoreUtil

import kotlinx.android.synthetic.main.interest.*

class Interest : AppCompatActivity() {

   lateinit var Radios : ArrayList<RadioButton>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.interest)
         Radios = arrayListOf<RadioButton>(Interest1 , Interest2 , Interest3 , Interest4 , Interest5)


        if (RadioGroup.checkedRadioButtonId != -1) {

            if (Interest1.isChecked) {}



        }
        ProvideWarnings().ProvideDialog("Provide your Interests to find your Perfect Partner ", this , false , "Ok" , false , "Interests Page" , false )


        // First Try


        // Make sure first one is five ®
        var FirstCategory = 0
        var FirstStandard = 0

        for (n in Radios.indices) {


                Interests.CategoryGatheredTypes[FirstCategory] =
                    Interests.AllCategoryTypes[FirstCategory][FirstStandard] // Assigns User interest values
            FirstStandard += 1

        }




        var CategoryNumber = 0 // Current Category On
        // Next Clicks
        var FirstOne = true
        NextInterest.setOnClickListener {



            var CategoryTypeNumber = 0 // To get the category Types



            if (CheckAllRadioClicked() == false ) {} // IF and only if the page is completed // This is to check all the current radio grou atleast one is marked

            else {



                for (n in Radios.indices) {


                    if (CategoryNumber != 0 && !FirstOne) {
                        if (Radios[n].isChecked) {
                            Interests.CategoryGatheredTypes[CategoryNumber - 1] =
                                Interests.AllCategoryTypes[CategoryNumber - 1][n] // Assigns User interest values

                        }

                    } else if (!FirstOne){
                        if (Radios[n].isChecked) {
                            Interests.CategoryGatheredTypes[CategoryNumber] =
                                Interests.AllCategoryTypes[CategoryNumber][n] // Assigns User interest values

                        }

                    }
                }
                FirstOne = false

                for (n in Radios.indices) {
                    if (n !=  0) {
                        Radios[n].isChecked = false
                        Radios[n].visibility = View.GONE
                    }
                }

//                for (n in Radios) {
//                    n.visibility = View.VISIBLE
//                }

                Radios[0].isChecked = true // First one is checked by default
                CategoryNameText.text = Interests.Category[CategoryNumber] // assign name to category type


                for (n in 0..Interests.AllCategoryTypes[CategoryNumber].size - 1) { // if it crashes it means you have less options

                       Radios[n].visibility = View.VISIBLE
                        Radios[n].text =
                            Interests.AllCategoryTypes[CategoryNumber][CategoryTypeNumber] // Assigns values to radio buttons

//                        if (Radios[n].isChecked) {
//                            Interests.CategoryGatheredTypes[CategoryNumber] =
//                                Interests.AllCategoryTypes[CategoryNumber][CategoryTypeNumber] // Assigns User interest values
//                        }
//
                    CategoryTypeNumber += 1

                }

                if (Interests.Category.count() - 1 > CategoryNumber) {
                    CategoryNumber += 1
                } else {

                    for (n in Radios.indices) {


                        if (Radios[n].isChecked ) {
                            Interests.CategoryGatheredTypes[CategoryNumber] =
                                Interests.AllCategoryTypes[CategoryNumber][n] // Assigns User interest values

                        }
                    }


                    // Upload Interests online
                    FireabaseClass.AssignInterest()
                    // Upload to Firestore user
                    FireStoreUtil.UpdateInterest(Interests.CategoryGatheredTypes)


                    val intent = Intent(this,  my.promise.harshil.SetImage::class.java)

                    startActivity(intent)


                }


            }

        }

        // If went back to the user




    }



    fun CheckAllRadioClicked() : Boolean {

        var OneClickedMin = false // True if any one check box is clicked
        for (n in Radios) {
            if (n.isChecked) {
                OneClickedMin = true
            }
        }
        return OneClickedMin

    }


}


// Interest Class
val Interests = InterestList()



class InterestList() {

    // If you add here , Add Gathered too || Follow the same order for category Gathered types

    private val Type = arrayListOf<String>("Funny" , "Serious" , "Treywey")


    private val Like = arrayListOf<String>("Me" , "You" , "Both")


    // Only All Types



        // Interest Gathered




    val Category = arrayListOf<String>("Favourite Genre of of Music" , "Ill spend most of my time at prom in", "Favourite past time Activity", "Favorite TV show Past Time" , "I am into reading" , "Are you an Animal Person?" , "According to you is the glass" , "Are you a" , "You would consider yourself" , "I'm an" ,"I'd like my prom outfit to be:" ,  "Would you rather" )
    val MusicGenre = arrayListOf<String>("Pop" , "Romantic"  , "Rap"  , "Classical" , "Rock")
    val MostTimeInProm = arrayListOf<String>("Dance" , "Eating" , "Talking" , "Taking Pictures" , "Dance and Pictures")
    //val IamAn = arrayListOf<String>("Extrovert" , "Introvert" ,"Ambivent")
    val FavPastTime = arrayListOf<String>("Playing Music" , "Writing" , "Video Games" , "Long Walks" , "Movies")
    val FavTvShowPastTime = arrayListOf<String>("Drama" , "Romantic" , "Comedy" , "Crime" , "Documentary")
   // private val Sports = arrayListOf<String>("Football" , "Basketball"  , "Cricket" , "Badminton" , "Tennis")
    private val Stream = arrayListOf<String>("Yes" , "Not really")


    val AnimalPerson = arrayListOf<String>("Yes" , "Not really")

    val GlassIs = arrayListOf<String>("Half Empty" , "Half Full")

    val AreYouA = arrayListOf<String>("Night Person" , "Morning Person" )

    val ConsiderYourSelf = arrayListOf<String>("Romantic" , "Moderately Romantic" , "Not really Romantic")

    val FitIn = arrayListOf<String>("Fit In" , "Stand Out")

    val IAmAn = arrayListOf<String>("Extrovert" , "Introvert" , "Ambivert")
    val Dress= arrayListOf<String>("coordinated with my date" ,"random")


    // Collected Item
    var AllCategoryTypes = arrayListOf<ArrayList<String>>(MusicGenre , MostTimeInProm , FavPastTime , FavTvShowPastTime  , Stream , AnimalPerson , GlassIs , AreYouA , ConsiderYourSelf , IAmAn ,Dress ,  FitIn)



    var CategoryGatheredTypes = arrayListOf<String>("Pop" , "Dance" , "Playing Music" , "Drama"  , "Arts" ,"Yes" , "Half Empty"  , "Night Person" , "Romantic" ,  "Extrovert" , "coordinated with my date" , "Fit In") // Should be equal to Category // Should match the order // Should be the first one in option too for instance first time in like is me so here or it should be me


}
