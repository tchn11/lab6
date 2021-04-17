package client.console;

import general.data.*;
import general.exeptions.EmptyIOException;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Operate console print or parse from int
 */
public class ConsoleManager {
    private Scanner scanner;
    private String[] command;


    /**
     * Constructor, just get scanner to use
     * @param sc Scanner
     */
    public ConsoleManager(Scanner sc){
        scanner = sc;
    }

    /**
     * Static method to print from any part of program
     * @param msg Message
     */
    static public void Print(String msg)
    {
        System.out.println(msg);
    }

    /**
     * Static method to print error notification
     * @param msg Message
     */
    static public void PrintErr(String msg)
    {
        System.out.println("Err: " + msg);
    }

    /**
     * Method that ask group from user
     * @return Group
     */
    public RowStudyGroup askGroup()
    {

        String name = parseString("Введите имя:");

        long x;

        while (true){
            x = parseLong("Введите кординату X");
            if (x > 752){
                Print("Число должно быть меньше 752");
            }
            else {
                break;
            }
        }

        int y = parseInteger("Введите кординату Y");

        Integer stdC;

        while (true){
            stdC = parseInteger("Введите количество студентов:");
            if(stdC <= 0){
                Print("Число болжно быть больше 0");
            }
            else{
                break;
            }
        }

        Integer expSt;

        while (true){
            expSt = parseInteger("Введите количество отчисленных студентов:");
            if(expSt <= 0){
                Print("Число болжно быть больше 0");
            }
            else{
                break;
            }
        }

        Long evMark;

        while (true){
            evMark = parseLong("Введите среднюю оценку:");
            if(evMark <= 0){
                Print("Число болжно быть больше 0");
            }
            else{
                break;
            }
        }

        Semester sem = parseSemester("Введите сeместр");

        String admName = parseString("Введите имя админа:");

        java.time.LocalDateTime admDate = parseDate("Введите дату рождения");

        Long admWeight;

        while (true){
            admWeight = parseLong("Введите вес админа:");
            if(admWeight <= 0){
                Print("Число болжно быть больше 0");
            }
            else{
                break;
            }
        }

        String admPassID = parseString("Введите номер паспорта");

        return new RowStudyGroup(name, new Coordinates(x, y), stdC, expSt, evMark, sem, new Person(admName, admDate, admWeight, admPassID));
    }


    /**
     * Ask String from console
     * @param msg Message what user should enter
     * @return Result of parsing
     */
    public String parseString(String msg){
        String str = null;

        while (str == null){
            try {
                Print(msg);
                String buf = scanner.nextLine().trim();
                if (buf.equals(""))
                    throw new EmptyIOException();
                if (buf == null)
                    throw new EmptyIOException();
                str = buf;
            }
            catch (EmptyIOException e){
                PrintErr("Ввод не может быть пустым");
            }

        }

        return str;
    }

    /**
     * Pars integer number from console
     * @param msg Message what user should enter
     * @return Result of parsing
     */
    public Integer parseInteger(String msg){
        Integer out = null;

        while (out == null){
            try {
                Print(msg);
                String buf = scanner.nextLine().trim();
                if (buf.equals(""))
                    throw new EmptyIOException();
                if (buf == null)
                    throw new EmptyIOException();
                out = Integer.parseInt(buf);
            }
            catch (EmptyIOException e){
                PrintErr("Ввод не может быть пустым");
            }
            catch (NumberFormatException e){
                PrintErr("Введите число");
            }

        }

        return out;
    }

    /**
     * Pars Long number from console
     * @param msg Message what user should enter
     * @return Result of parsing
     */
    public Long parseLong(String msg){
        Long out = null;

        while (out == null){
            try {
                Print(msg);
                String buf = scanner.nextLine().trim();
                if (buf.equals(""))
                    throw new EmptyIOException();
                if (buf == null)
                    throw new EmptyIOException();
                out = Long.parseLong(buf);
            }
            catch (EmptyIOException e){
                PrintErr("Ввод не может быть пустым");
            }
            catch (NumberFormatException e){
                PrintErr("Введите число");
            }

        }

        return out;
    }

    /**
     * Pars one of elements of enum Semester.
     * @param msg Message what user should enter
     * @return Result of parsing
     */
    public Semester parseSemester(String msg){
        Semester out = null;

        while (out == null){
            try {
                Print(msg);
                Print("Варианты:\n" + Semester.nameList());
                String buf = scanner.nextLine().trim();
                if (buf.equals(""))
                    throw new EmptyIOException();
                if (buf == null)
                    throw new EmptyIOException();
                out = Semester.valueOf(buf);
            }
            catch (EmptyIOException e){
                PrintErr("Ввод не может быть пустым");
            }
            catch (IllegalArgumentException e){
                PrintErr("Введите элемент из списка");
            }

        }

        return out;
    }
    /**
     * Pars date and time from console
     * @param msg Message what user should enter
     * @return Result of parsing
     */
    public java.time.LocalDateTime parseDate(String msg){
        java.time.LocalDateTime out = null;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        while (out == null){
            try {
                Print(msg);
                Print("Формат: yyyy-MM-dd HH:mm");
                String buf = scanner.nextLine().trim();
                if (buf.equals(""))
                    throw new EmptyIOException();
                if (buf == null)
                    throw new EmptyIOException();
                out = java.time.LocalDateTime.parse(buf, formatter);
            }
            catch (EmptyIOException e){
                PrintErr("Ввод не может быть пустым");
            }
            catch (java.time.format.DateTimeParseException e){
                PrintErr("Введите дату");
            }

        }

        return out;
    }

    public static void print(String str){
        System.out.println(str);
    }

    public static void printErr(String str){
        System.out.println("error: " + str);
    }

    public void waitCommand(){
        command = new String[]{"", ""};
        while (command[0].equals("")){
            String line = scanner.nextLine();
            if (!line.trim().equals("")){
                command = (line .trim()+ " ").split(" ", 2);
            }
        }
        print(command.toString());
    }

    public String getCommand(){
        return command[0].trim();
    }

    public String getArg(){
        return command[1].trim();
    }
}
