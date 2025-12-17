package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;


import java.util.List;

// add the annotations to make this a REST controller
@RestController
// add the annotation to make this controller the endpoint for the following url
// http://localhost:8080/categories
@RequestMapping("/categories")
// add annotation to allow cross site origin requests
@CrossOrigin
public class CategoriesController {
    private CategoryDao categoryDao;
    private ProductDao productDao;

    @Autowired  // create an Autowired controller to inject the categoryDao and ProductDao
    public CategoriesController(CategoryDao categoryDao, ProductDao productDao) {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
    }

    @GetMapping // add the appropriate annotation for a get action
    public List<Category> getAll() {
        return categoryDao.getAllCategories(); // find and return all categories
    }

    @GetMapping("/{id}") // add the appropriate annotation for a get action
    public Category getById(@PathVariable int id) {
            var category = categoryDao.getById(id);

            if (category == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);

            return category;
    }

    // the url to return all products in category 1 would look like this
    // https://localhost:8080/categories/1/products
    @GetMapping("/{categoryId}/products")
    public List<Product> getProductsById(@PathVariable int categoryId) {
        return productDao.listByCategoryId(categoryId);  // return all products that belong to this {categoryId}
    }

    // add annotation to call this method for a POST action
    @PostMapping()
    // add annotation to ensure that only an ADMIN can call this function
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Category addCategory(@RequestBody Category category) {
        try {
            return categoryDao.create(category);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    // add annotation to call this method for a PUT (update) action - the url path must include the categoryId
    @PutMapping("/{id}")
    // add annotation to ensure that only an ADMIN can call this function
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateCategory(@PathVariable int id, @RequestBody Category category) {
        try {
            categoryDao.update(id, category);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    // add annotation to call this method for a DELETE action - the url path must include the categoryId
    @DeleteMapping("/{id}")
    // add annotation to ensure that only an ADMIN can call this function
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable int id) {
            var category = categoryDao.getById(id);

            if (category == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);

            categoryDao.delete(id);
    }
}
