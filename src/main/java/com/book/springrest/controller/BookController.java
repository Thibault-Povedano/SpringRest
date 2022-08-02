package com.book.springrest.controller;


import com.book.springrest.entity.Book;
import com.book.springrest.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/books")

public class BookController {

    @Autowired
    BookRepository bookRepository;

    @GetMapping
    public List<Book> getAll(){
        return bookRepository.findAll();
    }

    @GetMapping("/{id}")
    public Book getById(@PathVariable Long id){
       Optional<Book> optionalBook = bookRepository.findById(id);

       if(optionalBook.isPresent()) return optionalBook.get();
       return null;
    }

    @PutMapping("/{id}")
    public Book updateById(@PathVariable Long id, String title, String description, String author ){
        Optional<Book> bookOptional = bookRepository.findById(id);

        if(bookOptional.isEmpty()) return null;
        Book book = bookOptional.get();
          book.setTitle(title);
          book.setDescription(description);
          book.setAuthor(author);
        return bookRepository.save(book);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id){
        try {
            bookRepository.deleteById(id);
        }catch (Exception exception){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public Book create(String title, String description, String author){
        Book book = new Book(title, description, author);
        return bookRepository.save(book);
    }

    @GetMapping("/{keyWord}")
    public List <Book> searchByKeyWord(@RequestBody Map<String , String> body){
        String keyWord = body.get("text");
        return bookRepository.findByTitleContainingOrDescriptionContaining(keyWord, keyWord);

    }

}
