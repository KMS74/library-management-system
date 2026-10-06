package com.example.restdemo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.restdemo.dto.BookDTO;
import com.example.restdemo.exception.ResourceNotFoundException;
import com.example.restdemo.model.Book;
import com.example.restdemo.repository.BookRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public List<Book> getAllBooks(String genre) {
        if (genre != null && !genre.isBlank()) {
            return this.bookRepository.findBooksByGenre(genre);
        }
        return this.bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }

    @Transactional
    public Book createBook(BookDTO bookDTO) {
        Book book = Book.builder()
                .title(bookDTO.getTitle())
                .author(bookDTO.getAuthor())
                .publicationYear(bookDTO.getPublicationYear())
                .genre(bookDTO.getGenre())
                .availableCopies(bookDTO.getAvailableCopies())
                .build();
        return bookRepository.save(book);
    }

    @Transactional
    public Book updateBook(Long id, BookDTO bookDTO) {
        Book existingBook = getBookById(id);
        existingBook.setTitle(bookDTO.getTitle());
        existingBook.setAuthor(bookDTO.getAuthor());
        existingBook.setPublicationYear(bookDTO.getPublicationYear());
        existingBook.setGenre(bookDTO.getGenre());
        existingBook.setAvailableCopies(bookDTO.getAvailableCopies());
        return bookRepository.save(existingBook);
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = getBookById(id);
        bookRepository.delete(book);
    }
}
