package com.meetingscheduler.Utils

import com.meetingscheduler.Model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class DatabseHelper {
    private val dbUrl = "jdbc:postgresql://localhost:5432/dbstudents"
    private val dbUser = "app_user"
    private val dbPassword = "app_password"

    init {
        try {
            // Charger le driver PostgreSQL
            Class.forName("org.postgresql.Driver")
            println("Driver PostgreSQL chargé avec succès.")
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            println("Échec du chargement du driver PostgreSQL.")
        }

        // Tester la connexion sur un thread en arrière-plan
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val connection = DriverManager.getConnection(dbUrl)
                println("Connexion réussie à la base de données.")
                // Vous pouvez maintenant utiliser la connexion pour effectuer des requêtes SQL
                connection.close() // N'oubliez pas de fermer la connexion
            } catch (e: SQLException) {
                println("Échec de la connexion à la base de données.")
                println("Erreur SQL : ${e.message}") // Affichez le message d'erreur ici
                e.printStackTrace()
            }
        }
    }


    private fun testDatabaseConnection() {
        // Test de l'URL
        try {
            val connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)
            println("Connexion réussie à la base de données.")
            connection.close()
        } catch (e: SQLException) {
            println("Échec de la connexion à la base de données.")
            if (e.message?.contains("username") == true) {
                println("Vérifiez que l'utilisateur est correct : $dbUser")
            }
            if (e.message?.contains("password") == true) {
                println("Vérifiez que le mot de passe est correct.")
            }
            if (e.message?.contains("database") == true) {
                println("Vérifiez que la base de données existe : dbStudents")
            }
            e.printStackTrace()
        }

        // Test de chaque variable individuellement
        checkDbUrl()
        checkDbUser()
        checkDbPassword()
    }

    private fun checkDbUrl() {
        try {
            DriverManager.getConnection(dbUrl, dbUser, dbPassword).close()
            println("L'URL de la base de données est valide.")
        } catch (e: SQLException) {
            println("URL de la base de données invalide : $dbUrl")
        }
    }

    private fun checkDbUser() {
        try {
            DriverManager.getConnection(dbUrl, "wrong_user", dbPassword).close()
        } catch (e: SQLException) {
            println("Nom d'utilisateur incorrect : $dbUser")
        }
    }

    private fun checkDbPassword() {
        try {
            DriverManager.getConnection(dbUrl, dbUser, "wrong_password").close()
        } catch (e: SQLException) {
            println("Mot de passe incorrect.")
        }
    }

    // Établir la connexion
    @Throws(SQLException::class)
    private suspend fun connect(): Connection? {
        return withContext(Dispatchers.IO) {
            DriverManager.getConnection(dbUrl, dbUser, dbPassword)
        }
    }

    // Exemple de récupération de données
    suspend fun getUserInfo(userId: String): User? {
        val connection = connect()
        val query = "SELECT * FROM Utilisateur WHERE id = ?"
        return connection?.use { conn ->
            val preparedStatement = conn.prepareStatement(query)
            preparedStatement.setString(1, userId)
            val resultSet = preparedStatement.executeQuery()
            if (resultSet.next()) {
                return@use User(
                    id_user = resultSet.getString("id"),
                    nom = resultSet.getString("name"),
                    prenom = resultSet.getString("group"),
                    email = resultSet.getString("email"),
                    type_user = resultSet.getString("type"),
                    role = resultSet.getString("role")
                )
            }
            null
        }
    }

    // Vérification de la connexion
    suspend fun testConnection(): Boolean {
        return try {
            connect()?.use { conn ->
                conn.isValid(2) // Vérifie la validité de la connexion dans les 2 secondes
            } ?: false
        } catch (e: SQLException) {
            e.printStackTrace()
            false
        }
    }
}
