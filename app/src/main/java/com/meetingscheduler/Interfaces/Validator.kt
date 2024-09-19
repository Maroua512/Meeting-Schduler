package com.meetingscheduler.Interfaces

interface Validator {
    fun validate(input:String): ValidationResult
}

 data class ValidationResult (val isValid:Boolean ,val errorMessage : String?=null)


