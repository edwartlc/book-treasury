package com.edwartlc.book_treasury.service;

public interface IDataConverter {
    <T> T getData(String json, Class<T> clase);
}
