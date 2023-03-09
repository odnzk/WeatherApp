package com.example.domain.util

open class AppException : RuntimeException()

class InvalidCityException : AppException()

class LocationPermissionDeniedException : AppException()

class LocationRequestFailedException : AppException()

class ConnectionLostException : AppException()
class NetworkException(val code: Int, val mes: String) : AppException()
