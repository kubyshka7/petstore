package com.example.demo.common;

/** Запрошенная запись не найдена – отдаётся клиенту как 404. */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
