package com.appecoviaje.data

class UserRepository(private val userDao: UserDao) {
    suspend fun createUser(user: User) {
        userDao.insert(user)
    }

    suspend fun getUser(username: String): User? {
        return userDao.getUserByUsername(username)
    }
}
