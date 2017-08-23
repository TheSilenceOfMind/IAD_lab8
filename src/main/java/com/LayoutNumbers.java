package com;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kirill.
 */
@ManagedBean (name="num")
@RequestScoped
public class LayoutNumbers {
    private List<Integer> numbers;


    public LayoutNumbers() {
        numbers = new ArrayList<Integer>();
        for(int i = -5; i <= 3; i++)
            numbers.add(i);
    }

    public List<Integer> getNumbers() {
        return numbers;
    }
}
