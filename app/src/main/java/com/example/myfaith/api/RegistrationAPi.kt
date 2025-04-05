import com.example.myfaith.entity.RegistrationResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RegistrationApi {
    @FormUrlEncoded
    @POST("/users/register")
    fun registerUser(
        @Field("email") email: String,
        @Field("number") number: String,
        @Field("userFullName") name: String,
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<RegistrationResponse>
}