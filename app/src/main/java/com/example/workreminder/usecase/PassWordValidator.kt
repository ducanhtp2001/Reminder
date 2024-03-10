package com.example.workreminder.usecase

class PassWordValidator {
    companion object {
        fun validator(pass: String): Boolean {
            val passwordRegex = Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,10}$")
            return passwordRegex.matches(pass) && pass.first().isUpperCase()
        }

        fun compareAndValidatorPassword(p: String, r: String): Boolean {
            if (!validator(p) || !validator(r)) return false
            if (p == r) return true
            return false
        }
    }
}