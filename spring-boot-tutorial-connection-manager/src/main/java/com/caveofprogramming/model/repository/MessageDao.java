package com.caveofprogramming.model.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.caveofprogramming.model.entity.Message;

@Repository
public interface MessageDao  extends CrudRepository<Message, Long>  {

}
