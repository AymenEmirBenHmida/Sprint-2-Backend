package com.example.books.restontrollers;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.books.entities.Author;
import com.example.books.entities.Book;
import com.example.books.service.BookService;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class BookRESTController {
@Autowired
BookService BookService;
@Autowired  ServletContext context;
@RequestMapping(method = RequestMethod.GET)
public List<Book> getAllBooks() {
return BookService.getAllBooks();
}
@RequestMapping(value="/{id}",method = RequestMethod.GET)
public Book getBookById(@PathVariable("id") Long id) {
return BookService.getBook(id);
}

@PostMapping(path="findBookName")
public List<Book> getBookByName(@RequestBody(required=false) String name) throws Exception{
	if(name=="	") {
		 return BookService.getAllBooks();
	}
	if(name==null) {
		 return BookService.getAllBooks();
	}
	 return BookService.findByNom(name);
}
@RequestMapping(method = RequestMethod.POST)
public Book createBook(@RequestParam("book") String book,@RequestParam("file") MultipartFile file) throws JsonParseException , JsonMappingException , Exception{
	
    Book Book = new ObjectMapper().readValue(book, Book.class);
    if(file!=null) {

	boolean isExit = new File(context.getRealPath("/Images/")).exists();
    if (!isExit)
    {
    	new File (context.getRealPath("/Images/")).mkdir();
    	System.out.println("mk dir.............");
    }
    String filename = file.getOriginalFilename();
    String newFileName = FilenameUtils.getBaseName(filename)+"."+FilenameUtils.getExtension(filename);
    File serverFile = new File (context.getRealPath("/Images/"+File.separator+newFileName));
    try
    {
    	System.out.println("Image");
    	 FileUtils.writeByteArrayToFile(serverFile,file.getBytes());
    	 
    }catch(Exception e) {
    	e.printStackTrace();
    }

   
    Book.setFileName(newFileName);
   
    }
return BookService.saveBook(Book);


}
@RequestMapping(method = RequestMethod.PUT)
public Book updateBook(@RequestParam("book") String book,@RequestParam(value="file",required = false) MultipartFile file) throws JsonParseException , JsonMappingException , Exception{
	
    Book Book = new ObjectMapper().readValue(book, Book.class);
    if(file!=null) {
	boolean isExit = new File(context.getRealPath("/Images/")).exists();
    if (!isExit)
    {
    	new File (context.getRealPath("/Images/")).mkdir();
    	System.out.println("mk dir.............");
    }
    String filename = file.getOriginalFilename();
    String newFileName = FilenameUtils.getBaseName(filename)+"."+FilenameUtils.getExtension(filename);
    File serverFile = new File (context.getRealPath("/Images/"+File.separator+newFileName));
    try
    {
    	System.out.println("Image");
    	 FileUtils.writeByteArrayToFile(serverFile,file.getBytes());
    	 
    }catch(Exception e) {
    	e.printStackTrace();
    }

   
    Book.setFileName(newFileName);
    }
    
return BookService.updateBook(Book);


}

@GetMapping(path="/ImageBook/{id}")
public byte[] getPhoto(@PathVariable("id") Long id) throws Exception{
	 Book Book   = BookService.findByIdBook(id);
	 return Files.readAllBytes(Paths.get(context.getRealPath("/Images/")+Book.getFileName()));
}


@RequestMapping(value="/{id}",method = RequestMethod.DELETE)
public void deleteBook(@PathVariable("id") Long id)
{
BookService.deleteBookById(id);
}

@RequestMapping(value="/prodscat/{idCat}",method = RequestMethod.GET)
public List<Book> getBooksAuthorIdAuthor(@PathVariable("idCat") Long idCat) {
return BookService.findByAuthorIdAuthor(idCat);
}
@RequestMapping(value="author",method = RequestMethod.GET)
public List<Author> getAllAuthors() {
return BookService.getAllAuthors();
}
@RequestMapping(value="author/{id}",method = RequestMethod.GET)
public Author getAuthorById(@PathVariable("id") Long id) {
return BookService.getAuthor(id);

}

@RequestMapping(value="author",method = RequestMethod.POST)
public Author createAuthor(@RequestBody Author Author) {
return BookService.saveAuthor(Author);
}

@RequestMapping(value="author",method = RequestMethod.PUT)
public Author updateAuthor(@RequestBody Author Author) {
return BookService.updateAuthor(Author);
}

@RequestMapping(value="author/{id}",method = RequestMethod.DELETE)
public void deleteAuthor(@PathVariable("id") Long id)
{
BookService.deleteAuthorById(id);
}


}