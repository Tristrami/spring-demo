package com.seamew.xmlConfig.annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

public class PetShop
{
    @Autowired
    private List<Pet> pets;
    private Address address;
    private Person owner;
    private Licence licence;
    private Map<String, Pet> petDetails;
    private Building building;

    @Autowired
    public PetShop(Address address)
    {
        this.address = address;
    }

    public PetShop(List<Pet> pets, Address address)
    {
        this.pets = pets;
        this.address = address;
    }

    @Autowired
    @Qualifier("petLicence")
    public void initialize(Licence licence)
    {
        this.licence = licence;
    }

    public Person getOwner()
    {
        return owner;
    }

    @Autowired
    public void setOwner(Person owner)
    {
        this.owner = owner;
    }

    public List<Pet> getPets()
    {
        return pets;
    }

    public void setPets(List<Pet> pets)
    {
        this.pets = pets;
    }

    public Address getAddress()
    {
        return address;
    }

    public void setAddress(Address address)
    {
        this.address = address;
    }

    public Licence getLicence()
    {
        return licence;
    }

    public Map<String, Pet> getPetDetails()
    {
        return petDetails;
    }

    @Autowired
    public void setPetDetails(Map<String, Pet> petDetails)
    {
        this.petDetails = petDetails;
    }

    public Building getBuilding()
    {
        return building;
    }

    @Resource(name = "petStoreBuilding")
    public void setBuilding(Building building)
    {
        this.building = building;
    }

    @Override
    public String toString()
    {
        return "PetShop{" +
                "pets=" + pets +
                ", address=" + address +
                ", owner=" + owner +
                ", licence=" + licence +
                ", petDetails=" + petDetails +
                ", building=" + building +
                '}';
    }
}
