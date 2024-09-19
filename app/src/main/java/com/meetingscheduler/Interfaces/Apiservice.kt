import com.meetingscheduler.Model.User
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("api/utilisateurs/")
    suspend fun getUtilisateurs(): Response<List<User>>
}
