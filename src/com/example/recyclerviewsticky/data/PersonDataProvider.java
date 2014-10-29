package com.example.recyclerviewsticky.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.util.Log;

public class PersonDataProvider {

	private List<Person> addedItems;
	private int age[] = {5,10,15,20};
	public PersonDataProvider() {
		// this.items = new LinkedHashMap<String, Boolean>();
		this.addedItems = new ArrayList<Person>();

		for (int i = 0; i < persons.length; i++) {
			Person p = new Person();
			p.setName(persons[i]);
			if(i%15 == 0){
				p.setAge(15);
			}else if(i%10 == 0){
				p.setAge(10);
			}else if(i%5 == 0){
				p.setAge(5);
			}else{
				p.setAge(20);
			}
			
			addedItems.add(p);
		}
		sortList();
	}

	public List<Person> getItems() {
		return addedItems;
	}

	public void remove(int position) {
		addedItems.remove(position);
	}

	public void add(Person p){
		//add to array
		addedItems.add(p);
		//sort
		sortList();
		//get position from array
	}
	
	private static String[] persons = {

	"Arlean Drewes", "Armida Carasco", 

	"Bridget Goulette", "Bryan Rarick",

	"Clint Cullen", "Clora Graybeal", 

	"Dexter Bencomo", "Dione Rhines", "Donella Blumstein", 

	"Emmy Denk", "Farrah Delosantos", 

	"Hai Oday", "Halley Holscher", "Hellen Baillie",

	"Hui Lupien", "Ileen Mccasland",

	"Jacquelyn Butter", "Jade Churchwell",

	"Jules Friley", "Julio Krier",

	"Kimber Boulware", "Kitty Manthe",

	"Long Show", "Louanne Garling", "Louella Petillo",

	"Nedra Dyson", "Norene Nelms", 

	"Richard Domingues", "Rochell Molock",

	"Saundra Lundahl", "Scotty Ralph",

	"Sherrie Poole", "Shirley Cliett", 

	"Trish Perino", "Tyesha Bruemmer", "Valda Skyles",

	"Willetta Zucker", "Yen Staton", "Yolonda Hadnott" };
	
	private void sortList(){
		Collections.sort(addedItems, new Comparator<Person>() {

			@Override
			public int compare(Person lhs, Person rhs) {
				
				return lhs.getAge() - rhs.getAge();
			}
		});
	}
	
	public int getRandomAge(){
		int randomPos = randInt(0, 3);
		
		return age[randomPos];
	}
	
	private int randInt(int min, int max) {

	    // NOTE: Usually this should be a field rather than a method
	    // variable so that it is not re-seeded every call.
	    Random rand = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}

}