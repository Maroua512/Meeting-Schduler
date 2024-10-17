package com.meetingscheduler.Utils
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class exmpl {

    companion object {
        private const val API_KEY = "api"  // Remplacez par votre clé API
        private const val DOMAIN = "sandboxd63e20d5a39945fcbe6eac3b6b2a9f94.mailgun.org"
    }

    fun sendSimpleMessage() {
        val client = OkHttpClient()

        // Construire le corps de la requête
        val requestBody = FormBody.Builder()
            .add("from", " Meetings Schduler <USER@$DOMAIN>")
            .add("to", "marwam@gmail.com")  // Destinataire
            .add("subject", "hello")
            .add("text", "testing")
            .build()

        // Créer la requête HTTP
        val request = Request.Builder()
            .url("https://api.mailgun.net/v3/$DOMAIN/messages")
            .post(requestBody)
            .addHeader("Authorization", Credentials.basic("api", API_KEY))
            .build()

        // Envoyer la requête de manière asynchrone
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to send email: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        println("Error: Unexpected code $response")
                        return
                    }

                    val responseBody = response.body?.string()
                    println("Response: $responseBody") // Afficher la réponse JSON

                    // Traiter la réponse JSON si nécessaire
                    responseBody?.let {
                        val json = JSONObject(it)
                        println("Parsed JSON: $json")
                    }
                }
            }
        })
    }
}
