package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T>{
    private final Comparator<T> cm;
    public MaxArrayDeque(Comparator<T> c){
        super();
        cm = c;
    }
    public T max(){
        if(this.size()==0){
            return null;
        }
        T max = this.get(0);
        for(T item : this){
            if(cm.compare(item,max) > 0){
                max = item;
            }
        }
        return max;
    }

    public T max(Comparator<T> c){
        if(this.size() == 0){
            return null;
        }
        T max = get(0);
        for(T item : this){
            if(c.compare(item,max)>0){
                max = item;
            }
        }
        return max;
    }
}
