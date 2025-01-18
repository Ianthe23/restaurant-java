package utils.observer;

import utils.events.IEvent;

public interface IObservable <E extends IEvent>{
    void addObserver(IObserver<E> e);
    void removeObserver(IObserver<E> e);
    void notifyObservers(E e);
}
