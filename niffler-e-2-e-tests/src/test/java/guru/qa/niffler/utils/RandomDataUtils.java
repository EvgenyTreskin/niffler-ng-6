package guru.qa.niffler.utils;

import com.github.javafaker.Faker;

public class RandomDataUtils {

    private static final Faker faker = new Faker();

    public static String randomUsername(){
        return faker.lordOfTheRings().character();
    }
    public static String randomName(){
        return faker.name().firstName();
    }
    public static String randomSurname(){
        return faker.name().lastName();
    }
    public static String randomCategoryName(){
        return faker.lordOfTheRings().location();
    }
    public static String randomSentence(int wordsCount){
        return faker.lorem().sentence(wordsCount);
    }

    public static String randomPassword() {
        return faker.internet().password(3, 5);
    }
}
