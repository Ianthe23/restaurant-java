package utils.observer;

import utils.events.IEvent;

public interface IObserver <E extends IEvent>{
    void update(E e);
}
