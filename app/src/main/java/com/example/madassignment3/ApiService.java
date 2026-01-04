// ApiService.java
package com.example.madassignment3;

import retrofit2.Call;
import retrofit2.http.GET;
import java.util.List;

public interface ApiService {
    @GET("users")
    Call<List<User>> getUsers();
}