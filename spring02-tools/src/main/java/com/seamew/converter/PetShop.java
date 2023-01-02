package com.seamew.converter;

public class PetShop
{
    private Animal animal;

    public PetShop()
    {
    }

    public PetShop(Animal animal)
    {
        this.animal = animal;
    }

    public Animal getAnimal()
    {
        return animal;
    }

    public void setAnimal(Animal animal)
    {
        this.animal = animal;
    }

    @Override
    public String toString()
    {
        return "PetShop{" +
                "animal=" + animal +
                '}';
    }
}
