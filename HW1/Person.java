/* 
* Part 1. Finding Distinct Objects from Assignment 1: Defensive Programming.
* Assignment details: https://docs.google.com/document/d/1dHNmVrQabqjfng21eX5wt7ms1D_fRJKk8Ssu9siH0js/
* 
* Implements a method that takes in a list of Objects of class Person and returns a list of unique and 
* non-null elements of type Person.
* 
* @author lcosta
* Due Jan 31, 2023
*/

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Person {
	private String name; // person's name
	private String birthplace; // where the person was born
	private Date birthdate; // when the person was born
	
	public Person(String name, String birthplace, Date birthdate) {
		this.name = name;
		this.birthplace = birthplace;
		this.birthdate = birthdate;
	}
	
	public String getName() {
		return name;
	}
	
	public String getBirthplace() {
		return birthplace;
	}
	
	public Date getBirthdate() {
		return birthdate;
	}

	public String toString() {
		return "\n(" + this.name + ", " + this.birthplace + ", " + this.birthdate + ")";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		// Two Person objects should be considered equal/duplicates if they have the same
		// values for their name, birthplace, and birthdate instance variables.
		
		if (!(other instanceof Person)) {
			return false;
		}

		Person p = (Person)other;
		
		if (this.name.equals(p.getName())
			&& this.birthplace.equals(p.getBirthplace())
			&& this.birthdate.equals(p.getBirthdate())) {
			return true;
		}
		return false;
	}
	
	/**
	 * Removes duplicates and null values from a Person list.
	 * @param persons a list of Objects of type Person
	 * @return the filtered list (empty for no valid inputs)
	 */
	private static List<Person> findDistinctPersons(List<Person> persons) {
		List<Person> unique = new LinkedList<>();

		try {
			for (int i = 0; i < persons.size(); i++) {
				Person current = persons.get(i);
				if (current == null) {
					continue;
				}
				if (!(unique.contains(current))) {
					unique.add(current);
					// System.out.print(current);
				}
			}
		}
		catch (NullPointerException x) {
		}
		
		return unique;
	}

	/**
	 * This method removes duplicates and null values from a Person list. This is mostly done by
	 * passing onto the findDistinctPersons method but this method prevents lists of 
	 * types other than Person. 
	 * @param list the list to be filtered
	 * @return the filtered list (empty for no valid inputs)
	 */
	@SuppressWarnings("unchecked")
	public static List<Person> findDistinct(List list) {
		List<Person> unique = new LinkedList<>();
		
		try {
			unique = findDistinctPersons((List<Person>)(List)list);
		}
		catch(ClassCastException x){
			System.out.println("Invalid list type");
		}

		return unique;
	} // Inspired by: https://stackoverflow.com/questions/1917844/how-to-cast-listobject-to-listmyclass

	/**
	 * The main method tests various (valid and invalid) inputs for the Person findDistinct() method.
	 */
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		// Case: input is null
		System.out.println("Test 1: " + Person.findDistinct(null));

		List<Person> list = new LinkedList<>();

		// Case: input list is empty
		System.out.println("Test 2: " + Person.findDistinct(list));

		// Case: input list contains a null value
		list.add(null);
		System.out.println("Test 3: " + Person.findDistinct(list));

		// Case: valid inputs
		Person p1 = new Person("Grace Hopper", "New York", new Date(1906, 12, 9));
		Person p2 = new Person("Anita Borg", "Chicago", new Date(1949, 1, 17));
		Person p3 = new Person("Grace Hopper", "New York", new Date(1906, 12, 9));
		list.add(p1);
		list.add(p2);
		list.add(p3);
		list.add(new Person("Ada Lovelace", "London", new Date(1815, 12, 10)));
		list.add(new Person("Grace", "New York", new Date(1906, 12, 9)));
		System.out.println("Test 4: " + Person.findDistinct(list));

		// Case: input list is of a different type than Person
		List<Date> list2 = new LinkedList<>();
		Date d = new Date(1949, 1, 17);
		list2.add(d);
		System.out.print("Test 5: ");
		System.out.println(Person.findDistinct(list2));
	}
}