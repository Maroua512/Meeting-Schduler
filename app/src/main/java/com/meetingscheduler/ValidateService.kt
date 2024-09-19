package com.meetingscheduler

import com.google.android.material.textfield.TextInputLayout
import com.meetingscheduler.Interfaces.Validator

class ValidateService(private val validators : List<Pair<TextInputLayout, Validator>>) {
    fun validete():Boolean{
       var isvalid = true
        for((inputLayout,validator) in validators){
            val input = inputLayout.editText?.text.toString()
            val result = validator.validate(input)
           if(!result.isValid){
               inputLayout.error = result.errorMessage
              isvalid = false
           }
            else{
               inputLayout.isErrorEnabled= false
           }
        }
        return isvalid
    }
}