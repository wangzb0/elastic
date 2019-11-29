package com.elastic.es.repository;

import java.io.Serializable;

public interface ESRepository<T, I extends Serializable> {

    T save(T t);

    T findById(I id);

    I deleteById(I id);
}
