package service;

import domain.MenuItem;
import domain.Order;
import repo.IMenuItemRepo;
import repo.IRepository;
import utils.events.RestaurantEvent;
import utils.observer.IObservable;
import utils.observer.IObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.stream.StreamSupport;

public class RestaurantService implements IService<Integer>, IObservable<RestaurantEvent> {
    private List<IObserver<RestaurantEvent>> observers = new ArrayList<>();
    private final IRepository tableRepo;
    private final IRepository orderRepo;
    private final IMenuItemRepo menuItemRepo;

    public RestaurantService(IRepository tableRepo, IRepository orderRepo, IMenuItemRepo menuItemRepo) {
        this.tableRepo = tableRepo;
        this.orderRepo = orderRepo;
        this.menuItemRepo = menuItemRepo;
    }

    public Iterable getTables() {
        return tableRepo.findAll();
    }

    public List<String> getCategoriesFromMenu() {
        return menuItemRepo.getCategories();
    }

    public List<MenuItem> getMenuItemsByCategory(String category) {
        return menuItemRepo.getAllByCategory(category);
    }

    public void addOrder(Integer tableId, List<MenuItem> menuItems) {

    }


    @Override
    public void addObserver(IObserver<RestaurantEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(IObserver<RestaurantEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(RestaurantEvent restaurantEvent) {
        observers.forEach(observer -> observer.update(restaurantEvent));
    }
}
