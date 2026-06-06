package com.example.task3api

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            UserScreen()
        }
    }
}

@Composable
fun UserScreen() {

    var users by remember {
        mutableStateOf<List<User>>(emptyList())
    }

    var message by remember {
        mutableStateOf("Loading...")
    }

    LaunchedEffect(Unit) {

        RetrofitInstance.api.getUsers()
            .enqueue(object : Callback<List<User>> {

                override fun onResponse(
                    call: Call<List<User>>,
                    response: Response<List<User>>
                ) {

                    if (response.isSuccessful) {

                        users = response.body() ?: emptyList()

                        message =
                            "Users loaded = ${users.size}"
                    } else {

                        message =
                            "Response Error = ${response.code()}"
                    }
                }

                override fun onFailure(
                    call: Call<List<User>>,
                    t: Throwable
                ) {

                    message =
                        "ERROR: ${t.message}"
                }
            })
    }

    if (users.isEmpty()) {

        Text(text = message)

    } else {

        LazyColumn {

            items(users) { user ->

                Text(
                    text =
                        "${user.name} - ${user.email}"
                )
            }
        }
    }
}