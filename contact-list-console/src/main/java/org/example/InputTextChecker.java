package org.example;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputTextChecker {

    public static boolean fullNameIsCorrect(String fullName) {

        String[] fullNameArray = fullName.trim().split("\\s");
        if (fullNameArray.length != 3) {
            System.out.println("Некорректный формат ввода ФИО");
            return false;
        }

        if (!nameIsCorrect(fullNameArray[0])) return false;

        if (!nameIsCorrect(fullNameArray[1])) return false;

        if (!nameIsCorrect(fullNameArray[2])) return false;

        return true;
    }

    public static boolean emailIsCorrect(String email){
        Pattern p = Pattern.compile(".+@.+");
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean isPhoneNumberIsCorrect(String phoneNumber){
        Pattern p = Pattern.compile("^((8|\\+\\d)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$");
        Matcher m = p.matcher(phoneNumber);
        return m.matches();
    }

    private static boolean nameIsCorrect(String text){

        boolean isFirstSymbol = true;

        for(int i = 0; i < text.length(); i++)
        {
            char c = text.charAt(i);

            if (!(Character.isLetter(c) || c == '-')){
                System.out.println("Никаких знаков, кроме ' ' и '-' вводить нельзя!");
                return false;
            }

            if (c == '-'){
                isFirstSymbol = true;
            }

            if (isFirstSymbol){
                isFirstSymbol = false;
                if (Character.isLowerCase(c)){
                    System.out.println("Фамилия, имя и отчество должны начинаться с прописной буквы!");
                    return false;
                }
            }
            else {
                if (Character.isUpperCase(c)){
                    System.out.println("Остальные буквы должны быть строчными!");
                    return false;
                }
            }
        }

        return true;
    }

}
