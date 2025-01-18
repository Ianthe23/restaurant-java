package service;

import repo.IRepository;
import utils.events.RestaurantEvent;
import utils.observer.IObservable;
import utils.observer.IObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

public class RestaurantService implements IService<Integer>, IObservable<RestaurantEvent> {
    private List<IObserver<RestaurantEvent>> observers = new ArrayList<>();
    private final IRepository tableRepo;
    private final IRepository orderRepo;
    private final IRepository menuItemRepo;
    private final IRepository orderItemRepo;

    public RestaurantService(IRepository tableRepo, IRepository orderRepo, IRepository menuItemRepo, IRepository orderItemRepo) {
        this.tableRepo = tableRepo;
        this.orderRepo = orderRepo;
        this.menuItemRepo = menuItemRepo;
        this.orderItemRepo = orderItemRepo;
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
