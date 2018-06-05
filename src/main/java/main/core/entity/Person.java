package main.core.entity;

public class Person implements EntityLongId {

    private Long _id;
    private String _lastName;
    private String _firstName;

    public Person() {
    }

    public Person(String firstName, String lastName) {
        _firstName = firstName;
        _lastName = lastName;
    }

    public Long getId() {
        return _id;
    }

    public void setId(Long id) {
        _id = id;
    }

    public String getFirstName() { return _firstName; }


    public void setFirstName(String firstName) { _firstName = firstName; }


    public String getLastName() { return _lastName; }


    public void setLastName(String lastName) { _lastName = lastName; }

    @Override
    public String toString() {
        return "firstName: " + _firstName + ", lastName: " + _lastName;
    }

}
