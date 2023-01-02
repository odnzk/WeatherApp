package com.example.domain.exceptions

open class AppException : RuntimeException()

class InvalidCityException : AppException()

class LocationPermissionDeniedException : AppException()

class LocationRequestFailedException : AppException()

class ConnectionLostException() : AppException()
