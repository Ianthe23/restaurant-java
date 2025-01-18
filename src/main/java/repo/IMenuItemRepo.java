package repo;

import domain.MenuItem;

import java.util.List;

public interface IMenuItemRepo extends IRepository<String, MenuItem>{
    List<MenuItem> getAllByCategory(String category);
    List<String> getCategories();
}
