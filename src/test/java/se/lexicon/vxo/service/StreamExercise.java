package se.lexicon.vxo.service;

import org.junit.jupiter.api.Test;
import se.lexicon.vxo.model.Gender;
import se.lexicon.vxo.model.Person;
import se.lexicon.vxo.model.PersonDto;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Your task is to make all tests pass (except task1 because its non-testable).
 * However, you have to solve each task by using a java.util.Stream or any of its variance.
 * You also need to use lambda expressions as implementation to functional interfaces.
 * (No Anonymous Inner Classes or Class implementation of functional interfaces)
 */
public class StreamExercise {

    private static List<Person> people = People.INSTANCE.getPeople();



    /**
     * Turn integers into a stream then use forEach as a terminal operation to print out the numbers
     */
    @Test
    public void task1() {
        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        //integers.stream().forEach(System.out::println);  //1. using method reference

        integers.stream().forEach(integer -> System.out.println(integer));  //2. using lambda

    }

    /**
     * Turning people into a Stream count all members
     */
    @Test
    public void task2() {
        long amount = people.stream()//Turning people into a Stream
                            .count();//count all members

        assertEquals(10000, amount);
    }

    /**
     * Count all people that has Andersson as lastName.
     */
    @Test
    public void task3() {
        long amount = people
                .stream()//Turning people into a Stream
                .filter(person -> person.getLastName().equals("Andersson"))// filter people with Andersson last name
                .count();//count all  Andersson members



        int expected = 90;

        assertEquals(expected, amount);
    }

    /**
     * Extract a list of all female
     */
    @Test
    public void task4() {
        int expectedSize = 4988;
        //1.Turning people into a Stream
        //2.Filter people after gender - female
        //3.Collect to list - Collecting elements of a stream into a List or Map.

        List<Person> females = people
                .stream()
                .filter(person -> person.getGender().equals(Gender.FEMALE))
                .collect(Collectors.toList());

        assertNotNull(females);
        assertEquals(expectedSize, females.size());
    }

    /**
     * Extract a TreeSet with all birthDates
     */
    @Test
    public void task5() {
        int expectedSize = 8882;

        //1.Turning people into a Stream
        //2.Mapping people birthday using method reference
        //3.Collect - Collecting elements of a stream into a List or Map.
        Set<LocalDate> dates = people
                .stream()
                .map(Person::getDateOfBirth)
                .collect(Collectors.toCollection(TreeSet ::new));

        assertNotNull(dates);
        assertTrue(dates instanceof TreeSet);
        assertEquals(expectedSize, dates.size());
    }

    /**
     * Extract an array of all people named "Erik"
     */
    @Test
    public void task6() {
        int expectedLength = 3;
        //1.Turning people into a Stream
        //2.Filter people after first name Erik
        //3.To Array: Collects the elements of the stream into an array.

        Person[] result = people
                .stream()
                .filter(person -> person.getFirstName().equals("Erik"))
                .toArray(Person[]::new);

        assertNotNull(result);
        assertEquals(expectedLength, result.length);
    }

    /**
     * Find a person that has id of 5436
     */
    @Test
    public void task7() {
        Person expected = new Person(5436, "Tea", "Håkansson", LocalDate.parse("1968-01-25"), Gender.FEMALE);
        //1.Turning people into a Stream
        //2.Filter people after id using ==
        //3.Find first - Finds any or the first element of the stream.
        Optional<Person> optional = people
                .stream()
                .filter(person -> person.getPersonId()==5436)
                .findFirst();


        assertNotNull(optional);
        assertTrue(optional.isPresent());
        assertEquals(expected, optional.get());
    }

    /**
     * Using min() define a comparator that extracts the oldest person i the list as an Optional
     */
    @Test
    public void task8() {
        LocalDate expectedBirthDate = LocalDate.parse("1910-01-02");
        //1.Turning people into a Stream
        //2.Finds the minimum or maximum element in the stream, optionally using a provided comparator
        Optional<Person> optional = people
                .stream()
                .min(Comparator.comparing(Person::getDateOfBirth));

        assertNotNull(optional);
        assertEquals(expectedBirthDate, optional.get().getDateOfBirth());
    }

    /**
     * Map each person born before 1920-01-01 into a PersonDto object then extract to a List
     */
    @Test
    public void task9() {
        int expectedSize = 892;

        //1.Turning people into a Stream
        //2.Filtering person before date -lambda
        //3.Mapping people birthday using Predicate and lambda expression
        //4.Collect - Collecting elements of a stream into a List or Map.

        LocalDate date = LocalDate.parse("1920-01-01");

        List<PersonDto> dtoList = people
                .stream()
                .filter(person -> person.getDateOfBirth().isBefore(date))
                .map(person -> new PersonDto(person.getPersonId(),person.getLastName()))
                .collect(Collectors.toList());

        assertNotNull(dtoList);
        assertEquals(expectedSize, dtoList.size());
    }

    /**
     * In a Stream Filter out one person with id 5914 from people and take the birthdate and build a string from data that the date contains then
     * return the string.
     */
    @Test
    public void task10() {
        String expected = "WEDNESDAY 19 DECEMBER 2012";
        int personId = 5914;
        //1.Turning people into a Stream
        //2.Filtering person id 5914
        //3. Map the person and format the date the birthdate
        //4.build a string from data that the date contains the
        Optional<String> optional = people
                .stream()
                .filter(person -> person.getPersonId()==5914)
                        .map(person -> {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy", Locale.ENGLISH);
                            return person.getDateOfBirth().format(formatter).toUpperCase();
                        })
                                .findFirst();



        assertNotNull(optional);
        assertTrue(optional.isPresent());
        assertEquals(expected, optional.get());
    }

    /**
     * Get average age of all People by turning people into a stream and use defined ToIntFunction personToAge
     * changing type of stream to an IntStream.
     */
    @Test
    public void task11() {
        ToIntFunction<Person> personToAge =
                person -> Period.between(person.getDateOfBirth(), LocalDate.parse("2019-12-20")).getYears();
        double expected = 54.42;
        //stream()
        //mapToInt(personToAge)
        //.average()
        //.orElse(
        double averageAge = people
                .stream()
                .mapToInt(personToAge)
                .average()
                .orElse(0.0);

        assertTrue(averageAge > 0);
        assertEquals(expected, averageAge, .01);
    }

    /**
     * Extract from people a sorted string array of all firstNames that are palindromes. No duplicates
     */
    @Test
    public void task12() {
        String[] expected = {"Ada", "Ana", "Anna", "Ava", "Aya", "Bob", "Ebbe", "Efe", "Eje", "Elle", "Hannah", "Maram", "Natan", "Otto"};
        // map -Extract from people
        //  filter and string builder a sorted string array of all firstNames
        // that are palindromes.
        // distinct and sort  to array No duplicates
        String[] result = people.stream()
                .map(Person::getFirstName)
                .filter(firstName -> new StringBuilder(firstName).reverse().toString().equalsIgnoreCase(firstName))
                .distinct()
                .sorted()
                .toArray(String[]::new);


        assertNotNull(result);
        assertArrayEquals(expected, result);
    }

    /**
     * Extract from people a map where each key is a last name with a value containing a list of all that has that lastName
     */
    @Test
    public void task13() {
        int expectedSize = 107;
        //Extract from people
        // a map where each
        // key is a last name with a
        // value containing a list of all that has that lastName

        Map<String, List<Person>> personMap = people.stream()
                .collect(Collectors.groupingBy(Person::getLastName));

        assertNotNull(personMap);
        assertEquals(expectedSize, personMap.size());
    }

    /**
     * Create a calendar using Stream.iterate of year 2020. Extract to a LocalDate array
     */
    @Test
    public void task14() {

        LocalDate[] _2020_dates = null;
        LocalDate start = LocalDate.of(2020, 1, 1);
        LocalDate end = LocalDate.of(2020, 12, 31);
        // long numOfDaysBetween = ChronoUnit.DAYS.between(start, end) + 1;

        _2020_dates = Stream.iterate(start, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(start, end) + 1)
                .toArray(LocalDate[]::new);

        assertNotNull(_2020_dates);
        assertEquals(366, _2020_dates.length);
        assertEquals(LocalDate.parse("2020-01-01"), _2020_dates[0]);
        assertEquals(LocalDate.parse("2020-12-31"), _2020_dates[_2020_dates.length - 1]);
    }

}
