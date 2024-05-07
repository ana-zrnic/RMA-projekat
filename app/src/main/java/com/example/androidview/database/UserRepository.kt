package com.example.androidview.database

import org.mindrot.jbcrypt.BCrypt

class UserRepository(private val userDao: UserDao) {

    fun registerUser(userName: String, email: String, password: String) {
        if (userDao.getUserByEmail(email) != null) {
            // Handle user already exists scenario
            return
        }
        val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12))
        val user = UserEntity(userName = userName, email = email, password = hashedPassword)
        userDao.insert(user)
    }

    fun loginUser(email: String, password: String): Boolean {
        val user = userDao.getUserByEmail(email)
        return user != null && BCrypt.checkpw(password, user.password)
    }

    fun getUserByEmail(email: String): UserEntity? {
        return userDao.getUserByEmail(email)
    }
}