package com.example.androidview.database

import org.mindrot.jbcrypt.BCrypt

class UserRepository(private val userDao: UserDao) {

    fun registerUser(userName: String, email: String, password: String) : Boolean{
        if (userDao.getUserByUsernameOrEmail(userName,email) != null) {
            // Handle user already exists scenario
            return false
        }
        val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12))
        val user = UserEntity(userName = userName, email = email, password = hashedPassword)
        userDao.insert(user)
        return true
    }

    fun loginUser(usernameOrEmail: String, password: String): Boolean {
        val user = if (usernameOrEmail.contains("@")) {
            // The input is an email
            userDao.getUserByEmail(usernameOrEmail)
        } else {
            // The input is a username
            userDao.getUserByUserName(usernameOrEmail)
        }
        return user != null && BCrypt.checkpw(password, user.password)
    }
    fun getUser(email: String): UserEntity? {
        return if (email.contains("@")) {
            userDao.getUserByEmail(email)
        } else {
            userDao.getUserByUserName(email)
        }
    }


}