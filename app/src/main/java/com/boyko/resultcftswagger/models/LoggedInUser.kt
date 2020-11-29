package com.boyko.resultcftswagger.models

data class LoggedInUser(
	val name: String,
	val password: String
) {
	override fun toString(): String {
		return "{ \"name\": \"${this.name}\", \"password\": \"${this.password}\"}"
	}
}