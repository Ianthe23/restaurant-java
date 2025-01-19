package service;

import domain.MenuItem;
import domain.Order;
import domain.Table;
import enums.Status;
import repo.IMenuItemRepo;
import repo.IRepository;
import utils.events.RestaurantEvent;
import utils.observer.IObservable;
import utils.observer.IObserver;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.Optional;
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

    public void addOrder(String tableId, List<MenuItem> menuItems) {
        Optional<Table> table = tableRepo.findOne(tableId);
        if (table.isEmpty()) {
            throw new RuntimeException("Table not found");
        }

        Order order = new Order("a", table.get(), LocalDateTime.now(), Status.PLACED, menuItems);
        Optional<Order> addedOrder = orderRepo.save(order);
        if (addedOrder.isEmpty()) {
            throw new RuntimeException("Order not added");
        }
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
