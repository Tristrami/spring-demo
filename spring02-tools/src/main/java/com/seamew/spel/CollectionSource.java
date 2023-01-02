package com.seamew.spel;

import java.util.List;
import java.util.Map;

public class CollectionSource
{
    private List<Integer> numbers = List.of(1, 2, 3, 10, 11, 12);
    private String[] cities = {"Zhejiang:Ningbo", "Shandong:Qingdao", "Zhejiang:Taizhou"};
    private Map<String, Integer> scores = Map.of("lulu", 100, "xiaolan", 60, "zhu", 59);

    public List<Integer> getNumbers()
    {
        return numbers;
    }

    public void setNumbers(List<Integer> numbers)
    {
        this.numbers = numbers;
    }

    public String[] getCities()
    {
        return cities;
    }

    public void setCities(String[] cities)
    {
        this.cities = cities;
    }

    public Map<String, Integer> getScores()
    {
        return scores;
    }

    public void setScores(Map<String, Integer> scores)
    {
        this.scores = scores;
    }
}
