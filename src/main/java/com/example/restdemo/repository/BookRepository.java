package com.example.restdemo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.restdemo.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

    // To get all books filtered by genre ignoring case
    @Query("SELECT b FROM Book b WHERE LOWER(b.genre) = LOWER(:genre)")
    List<Book> findBooksByGenre(@Param("genre") String genre);

}
