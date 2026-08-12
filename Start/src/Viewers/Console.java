package edu.neumont.csc150.viewers;//package ...
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

/************************************************************
 * Console app by Brett Beardall                            *
 * Contributed by:                                          *
 *      - Arthur Grover (Implemented Color32)               *
 * **********************************************************/

//CTRL SHIFT -  = Collapse all code
//CTRL SHIFT +  = Expand all code
@SuppressWarnings("unused")
public class Console {
    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    //If the console should request the value on the same line as the question or get it from the following line
    public static boolean getInputOnSameLine = false;
    //Pattern to validate the date with. (e.g. "MM-dd-yyyy", "M/dd/yyyy", "dd/MM/yyyy")
    public static String defaultDateFormat = "MM/dd/yyyy";
    //Boolean to know if the console has full ANSI support (Disable if unexpected behavior occurs)
    private static final boolean FULL_ANSI_COLOR_SUPPORT = true;

    //region Class utility properties and functions

    //region TextColors (expand)
    public enum TextColor {BLACK, RED, GREEN, YELLOW, BLUE, PURPLE, CYAN, WHITE, DEFAULT}
    private static final String BLACK = "\u001B[30m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    private static final String WHITE = "\u001B[37m";
    //endregion

    //region BackgroundColors  (expand)
    public enum BackgroundColor {BLACK, RED, GREEN, YELLOW, BLUE, PURPLE, CYAN, WHITE, DEFAULT}
    private static final String BLACK_BACKGROUND = "\u001B[40m";
    private static final String RED_BACKGROUND = "\u001B[41m";
    private static final String GREEN_BACKGROUND = "\u001B[42m";
    private static final String YELLOW_BACKGROUND = "\u001B[43m";
    private static final String BLUE_BACKGROUND = "\u001B[44m";
    private static final String PURPLE_BACKGROUND = "\u001B[45m";
    private static final String CYAN_BACKGROUND = "\u001B[46m";
    private static final String WHITE_BACKGROUND = "\u001B[47m";
    //endregion

    //region TextStyles (expand)
    public enum TextStyle {BOLD, ITALIC, UNDERLINE, STRIKETHROUGH, NONE}
    private static final String BOLD = "\u001B[1m";
    private static final String ITALIC = "\u001B[3m";
    private static final String UNDERLINE = "\u001B[4m";
    private static final String STRIKETHROUGH = "\u001B[9m";
    //endregion

    /**
     * Resets the text color and background color to console default values
     */
    private static final String RESET = "\u001B[0m";

    //BLACK, RED, GREEN, YELLOW, BLUE, PURPLE, CYAN, WHITE
    private static String getTextColorText(TextColor textColor){
        return switch (textColor) {
            case RED -> Console.RED;
            case GREEN -> Console.GREEN;
            case YELLOW -> Console.YELLOW;
            case BLUE -> Console.BLUE;
            case PURPLE -> Console.PURPLE;
            case CYAN -> Console.CYAN;
            case BLACK -> Console.BLACK;
            case WHITE -> Console.WHITE;
            default -> RESET;
        };
    }

    private static String getBackgroundColorText(BackgroundColor backgroundColor){
        return switch (backgroundColor) {
            case RED -> Console.RED_BACKGROUND;
            case GREEN -> Console.GREEN_BACKGROUND;
            case YELLOW -> Console.YELLOW_BACKGROUND;
            case BLUE -> Console.BLUE_BACKGROUND;
            case PURPLE -> Console.PURPLE_BACKGROUND;
            case CYAN -> Console.CYAN_BACKGROUND;
            case BLACK -> Console.BLACK_BACKGROUND;
            case WHITE -> Console.WHITE_BACKGROUND;
            default -> RESET;
        };
    }

    private static String getTextStyleText(TextStyle textStyle){
        return switch (textStyle) {
            case BOLD -> Console.BOLD;
            case ITALIC -> Console.ITALIC;
            case UNDERLINE -> Console.UNDERLINE;
            case STRIKETHROUGH -> Console.STRIKETHROUGH;
            default -> "";
        };
    }

    //endregion

    /**
     * Gets an CHAR input from the console.
     * Console will keep asking until a valid response is provided.
     * Uses the default text color
     * @param message  Message to show to the user as to what you are requesting
     * @return Returns a char value representing the users input
     */
    public static char getCharInput(String message){
        return getCharInput(message, TextColor.DEFAULT);
    }

    /**
     * Gets an CHAR input from the console.
     * Console will keep asking until a valid response is provided.
     * @param message  Message to show to the user as to what you are requesting
     * @param textColor ConsoleTextColor of the text when written
     * @return Returns a char value representing the users input
     */
    public static char getCharInput(String message, TextColor textColor){
        return getStringInput(message, false, textColor).charAt(0);
    }

    /**
     * Gets an BOOLEAN input from the console.
     * Console will keep asking until a valid response is provided. Case-insensitive.
     * Uses the default text color
     * @param message  Message to show to the user as to what you are requesting
     * @param positive The positive value to ask the user for (e.g. "Yes")
     * @param negative The negative value to ask the user for (e.g. "No");
     * @return Returns a boolean value representing the users input
     */
    public static boolean getBooleanInput(String message, String positive, String negative){
        return getBooleanInput(message, positive, negative, TextColor.DEFAULT);
    }

    /**
     * Gets an BOOLEAN input from the console.
     * Console will keep asking until a valid response is provided. Case-insensitive.
     * @param message  Message to show to the user as to what you are requesting
     * @param positive The positive value to ask the user for (e.g. "Yes")
     * @param negative The negative value to ask the user for (e.g. "No");
     * @param textColor ConsoleTextColor of the text when written
     * @return Returns a boolean value representing the users input
     */
    public static boolean getBooleanInput(String message, String positive, String negative, TextColor textColor){
        boolean response = false;
        boolean isValidResponse = false;
        do{
            String possibleAnswers = " (" + positive + " or " + negative + ")";
            String responseS = getStringInput(message + possibleAnswers, false, textColor);
            responseS = responseS.toLowerCase().trim();
            if(responseS.equals(positive.toLowerCase())){
                response = isValidResponse = true;
            } else if(responseS.equals(negative.toLowerCase())){
                isValidResponse = true;
            }
            if(!isValidResponse){
                Console.writeln("Not a valid answer. Please write '" + positive + "' OR '" + negative + "' only.", TextColor.RED);
            }
        }while(!isValidResponse);
        return response;
    }

    /**
     * Gets an BYTE input from the console.
     * Console will keep asking until a valid response is provided.
     * Uses the default text color
     * @param message  Message to show to the user as to what you are requesting
     * @return Returns a byte value representing the users input
     */
    public static byte getByteInput(String message) {
        return getByteInput(message, TextColor.DEFAULT);
    }

    /**
     * Gets an BYTE input from the console.
     * Console will keep asking until a valid response is provided.
     * @param message  Message to show to the user as to what you are requesting
     * @param textColor ConsoleTextColor of the text when written
     * @return Returns a byte value representing the users input
     */
    public static byte getByteInput(String message, TextColor textColor) {
        return getByteInput(message, Byte.MIN_VALUE, Byte.MAX_VALUE, textColor);
    }

    /**
     * Gets an BYTE input from the console.
     * Console will keep asking until a valid response is provided.
     * @param message  Message to show to the user as to what you are requesting
     * @param min Minimum allowed byte value
     * @param max Max allowed byte value
     * @param textColor ConsoleTextColor of the text when written
     * @return Returns a byte value representing the users input
     */
    public static byte getByteInput(String message, byte min, byte max, TextColor textColor) {
        Byte response = null;
        do {
            try {
                String responseS = getStringInput(message, false, textColor);
                response = Byte.parseByte(responseS);
                if(response < min || response > max){
                    response = null;
                    Console.writeln("Number must be between " + min + " and " + max + "!", TextColor.RED);
                }
            } catch (NumberFormatException n) {
                Console.writeln("You entered an invalid number.", TextColor.RED);
            }
        }while(response == null);
        return response;
    }

    /**
     * Gets an SHORT input from the console.
     * Console will keep asking until a valid response is provided.
     * Uses the default text color
     * @param message  Message to show to the user as to what you are requesting
     * @return Returns a short value representing the users input
     */
    public static short getShortInput(String message) {
        return getShortInput(message, TextColor.DEFAULT);
    }

    /**
     * Gets an SHORT input from the console.
     * Console will keep asking until a valid response is provided.
     * @param message  Message to show to the user as to what you are requesting
     * @param textColor ConsoleTextColor of the text when written
     * @return Returns a short value representing the users input
     */
    public static short getShortInput(String message, TextColor textColor) {
        return getShortInput(message, Short.MIN_VALUE, Short.MAX_VALUE, textColor);
    }

    /**
     * Gets an SHORT input from the console.
     * Console will keep asking until a valid response is provided.
     * @param message  Message to show to the user as to what you are requesting
     * @param min Minimum allowed short value
     * @param max Max allowed short value
     * @param textColor ConsoleTextColor of the text when written
     * @return Returns a short value representing the users input
     */
    public static short getShortInput(String message, short min, short max, TextColor textColor) {
        Short response = null;
        do {
            try {
                String responseS = getStringInput(message, false, textColor);
                response = Short.parseShort(responseS);
                if(response < min || response > max){
                    response = null;
                    Console.writeln("Number must be between " + min + " and " + max + "!", TextColor.RED);
                }
            } catch (NumberFormatException n) {
                Console.writeln("You entered an invalid number.", TextColor.RED);
            }
        }while(response == null);
        return response;
    }

    /**
     * Gets an DOUBLE input from the console.
     * Console will keep asking until a valid response is provided.
     * Uses the default text color
     * @param message  Message to show to the user as to what you are requesting
     * @return Returns a double value representing the users input
     */
    public static double getDoubleInput(String message) {
        return getDoubleInput(message, TextColor.DEFAULT);
    }

    /**
     * Gets an DOUBLE input from the console.
     * Console will keep asking until a valid response is provided.
     * @param message  Message to show to the user as to what you are requesting
     * @param textColor ConsoleTextColor of the text when written
     * @return Returns a double value representing the users input
     */
    public static double getDoubleInput(String message, TextColor textColor) {
        return getDoubleInput(message, Double.MIN_VALUE, Double.MAX_VALUE, textColor);
    }

    /**
     * Gets an DOUBLE input from the console.
     * Console will keep asking until a valid response is provided.
     * @param message   Message to show to the user as to what you are requesting
     * @param min       Minimum double value allowed
     * @param max       Maximum double value allowed
     * @param textColor ConsoleTextColor of the text when written
     * @return Returns a double value representing the users input
     */
    public static double getDoubleInput(String message, double min, double max, TextColor textColor) {
        Double response = null;
        do {
            try {
                String responseS = getStringInput(message, false, textColor);
                response = Double.parseDouble(responseS);
                if (response < min || response > max) {
                    response = null;
                    Console.writeln("Number must be between " + min + " and " + max + "!", TextColor.RED);
                }
            } catch (NumberFormatException n) {
                Console.writeln("You entered an invalid number.", TextColor.RED);
            }
        } while (response == null);
        return response;
    }

    /**
     * Gets an FLOAT input from the console.
     * Console will keep asking until a valid response is provided.
     * Uses the default text color
     * @param message  Message to show to the user as to what you are requesting
     * @return Returns a float value representing the users input
     */
    public static float getFloatInput(String message) {
        return getFloatInput(message, TextColor.DEFAULT);
    }

    /**
     * Gets an FLOAT input from the console.
     * Console will keep asking until a valid response is provided.
     * @param message  Message to show to the user as to what you are requesting
     * @param textColor ConsoleTextColor of the text when written
     * @return Returns a float value representing the users input
     */
    public static float getFloatInput(String message, TextColor textColor) {
        return getFloatInput(message, -Float.MAX_VALUE, Float.MAX_VALUE, textColor);
    }

    /**
     * Gets an FLOAT input from the console.
     * Console will keep asking until a valid response is provided.
     * @param message  Message to show to the user as to what you are requesting
     * @param min Minimum allowed float value
     * @param max Max allowed float value
     * @param textColor ConsoleTextColor of the text when written
     * @return Returns a float value representing the users input
     */
    public static float getFloatInput(String message, float min, float max, TextColor textColor) {
        Float response = null;
        do {
            try {
                String responseS = getStringInput(message, false, textColor);
                response = Float.parseFloat(responseS);
                if(response < min || response > max){
                    response = null;
                    Console.writeln("Number must be between " + min + " and " + max + "!", TextColor.RED);
                }
            } catch (NumberFormatException n) {
                Console.writeln("You entered an invalid number.", TextColor.RED);
            }
        }while(response == null);
        return response;
    }

    /**
     * Gets an LONG input from the console.
     * Console will keep asking until a valid response is provided.
     * Uses the default text color
     * @param message  Message to show to the user as to what you are requesting
     * @return Returns a long value representing the users input
     */
    public static long getLongInput(String message) {
        return getLongInput(message, TextColor.DEFAULT);
    }

    /**
     * Gets an LONG input from the console.
     * Console will keep asking until a valid response is provided.
     * @param message  Message to show to the user as to what you are requesting
     * @param textColor ConsoleTextColor of the text when written
     * @return Returns a long value representing the users input
     */
    public static long getLongInput(String message, TextColor textColor) {
        return getLongInput(message, Long.MIN_VALUE, Long.MAX_VALUE, textColor);
    }

    /**
     * Gets an LONG input from the console.
     * Console will keep asking until a valid response is provided.
     * @param message  Message to show to the user as to what you are requesting
     * @param min Miniumum allowed long value
     * @param max Maximum allowed long value
     * @param textColor ConsoleTextColor of the text when written
     * @return Returns a long value representing the users input
     */
    public static long getLongInput(String message, long min, long max, TextColor textColor) {
        Long response = null;
        do {
            try {
                String responseS = getStringInput(message, false, textColor);
                response = Long.parseLong(responseS);
                if(response < min || response > max){
                    response = null;
                    Console.writeln("Number must be between " + min + " and " + max + "!", TextColor.RED);
                }
            } catch (NumberFormatException n) {
                Console.writeln("You entered an invalid number.", TextColor.RED);
            }
        }while(response == null);
        return response;
    }

    /**
     * Gets an INT input from the console.
     * Console will keep asking until a valid response is provided.
     * Uses the default text color
     * @param message  Message to show to the user as to what you are requesting
     * @return Returns an int value representing the users input
     */
    public static int getIntInput(String message) {
        return getIntInput(message, TextColor.DEFAULT);
    }

    /**
     * Gets an INT input from the console.
     * Console will keep asking until a valid response is provided.
     * Uses the default text color
     * @param message  Message to show to the user as to what you are requesting
     * @param min Minimum number the user can provide
     * @param max Maximum number the user can provide
     * @return Returns an int value representing the users input
     */
    public static int getIntInput(String message, int min, int max){
        return getIntInput(message, min, max, TextColor.DEFAULT);
    }

    /**
     * Gets an INT input from the console.
     * Console will keep asking until a valid response is provided.
     * @param message  Message to show to the user as to what you are requesting
     * @param textColor ConsoleTextColor of the text when written
     * @return Returns an int value representing the users input
     */
    public static int getIntInput(String message, TextColor textColor) {
        return getIntInput(message, Integer.MIN_VALUE, Integer.MAX_VALUE, textColor);
    }

    /**
     * Gets an INT input from the console.
     * Console will keep asking until a valid response is provided.
     * @param message  Message to show to the user as to what you are requesting
     * @param min Minimum number the user can provide
     * @param max Maximum number the user can provide
     * @param textColor ConsoleTextColor of the text when written
     * @return Returns an int value representing the users input
     */
    public static int getIntInput(String message, int min, int max, TextColor textColor){
        Integer response = null;
        do {
            try {
                String responseS = getStringInput(message, false, textColor);
                response = Integer.parseInt(responseS);
                if(response < min || response > max){
                    response = null;
                    Console.writeln("Number must be between " + min + " and " + max + "!", TextColor.RED);
                }
            } catch (NumberFormatException n) {
                Console.writeln("You entered an invalid number.", TextColor.RED);
            }
        }while(response == null);
        return response;
    }

    /**
     * Gets a String input from the console.
     * Uses the default text color and does not allow empty
     * Console will keep asking until a valid response is provided
     * @param message Message to show to the user as to what you are requesting
     * @return The String the user typed meeting the requirements
     */
    public static String getStringInput(String message){
        return getStringInput(message, false);
    }

    /**
     * Gets a String input from the console.
     * Uses the default text color
     * Console will keep asking until a valid response is provided
     * @param message Message to show to the user as to what you are requesting
     * @param allowEmpty If the method should allow empty (e.g. enter key or space)
     * @return The String the user typed meeting the requirements
     */
    public static String getStringInput(String message, boolean allowEmpty) {
        return getStringInput(message, allowEmpty, TextColor.DEFAULT);
    }

    /**
     * Gets a String input from the console.
     * Console will keep asking until a valid response is provided
     * @param message Message to show to the user as to what you are requesting
     * @param allowEmpty If the method should allow empty (e.g. enter key or space)
     * @param textColor ConsoleTextColor of the text when written
     * @return The String the user typed meeting the requirements
     */
    public static String getStringInput(String message, boolean allowEmpty, TextColor textColor){
        String response = null;
        do{
            try {
                if(getInputOnSameLine){
                    write(message, textColor);
                } else {
                    writeln(message, textColor);
                }
                response = br.readLine();
                if (!allowEmpty && response.trim().isEmpty()) {
                    Console.writeln("You must enter a non empty answer!", TextColor.RED);
                    response = null;
                }
            }catch(IOException ex){
                writeln(ex.getMessage(), TextColor.RED);
            }
        }while(response == null);
        return response;
    }

    /**
     * Gets a String input from the console.
     * Console will keep asking until a valid response is provided
     * @param message Message to show to the user as to what you are requesting
     * @param minCharacters The minimum amount of characters allowed in this string.
     * @param maxCharacters The maximum amount of characters allowed in this string.
     * @param textColor ConsoleTextColor of the text when written
     * @return The String the user typed meeting the requirements
     */
    public static String getStringInput(String message, int minCharacters, int maxCharacters, TextColor textColor){
        String response;
        do {
            response = getStringInput(message, minCharacters <= 0, textColor);
            if(response.length() < minCharacters || response.length() > maxCharacters){
                Console.writeln("Your input must be between " + minCharacters + " and " + maxCharacters + " characters in length!", TextColor.RED);
                response = null;
            }
        }while(response == null);
        return response;
    }

    /**
     * Asks the user to select one of the Enum values from the given list.
     * List is shown in a menu style 1. Value (etc)
     * <p>Example:<p>
     * MyEnum selection = Console.getEnumValue("Select an option", MyEnum.values(),
     *                         Console.TextColor.YELLOW, Console.TextColor.GREEN, true);
     * <p>
     * @param message The message to show the user under the Options list.
     * @param enumList The list of possible enumerations to show the user. Use the EnumName.values() method to get this.
     * @param listColor The color of the options list values
     * @param messageColor The color of the message under the options list
     * @param allowNone If you allow the user to pick a "None" option to choose none of them.
     * @return Returns the selected enum value, or null if the "None" option was chosen
     * @param <T> This is a generic Enum type. Can be any Enumeration value.
     */
    public static <T extends Enum<T>> T getEnumValueAsMenu(String message, T[] enumList, TextColor listColor, TextColor messageColor, boolean allowNone){
        if(enumList != null && enumList.length > 0 && enumList[0] != null) {
            do {
                for(int i = 0; i < enumList.length; i++){
                    writeln((i + 1) + ": " + enumList[i], listColor);
                }
                if(allowNone) {
                    writeln((enumList.length + 1) + ": None", messageColor);
                }
                int response = getIntInput(message, 1, (allowNone ? enumList.length + 1 : enumList.length) , messageColor);
                if(response == enumList.length + 1){
                    //Return null if they picked the "NONE" one
                    return null;
                }
                try {
                    return enumList[response - 1];
                } catch (IllegalArgumentException ex) {
                    writeln("You must pick one of the options and type it exactly (case insensitive).", TextColor.RED);
                    writeln("");
                }
            } while (true);
        }
        return null;
    }

    /**
     * Asks the user to select one of the Enum values from the given list.
     * This assumes you are using SCREAMING_SNAKE_CASE in your enums.
     * <p>Example:<p>
     * MyEnum selection = Console.getEnumValue("Select an option", MyEnum.values(), false,
     *                         Console.TextColor.YELLOW, Console.TextColor.GREEN);
     * <p>
     * @param message The message to show the user under the Options list.
     * @param enumList The list of possible enumerations to show the user. Use the EnumName.values() method to get this.
     * @param allowEmpty If you allow the user to just hit entire and not pick one. If false, they are asked until they give a valid answer.
     * @param listColor The color of the options list values
     * @param messageColor The color of the message under the options list
     * @return Returns the selected enum value, or null if allowsEmpty was true and they just hit enter.
     * @param <T> This is a generic Enum type. Can be any Enumeration value.
     */
    public static <T extends Enum<T>> T getEnumValueAsList(String message, T[] enumList, boolean allowEmpty, TextColor listColor, TextColor messageColor){
        if(enumList != null && enumList.length > 0 && enumList[0] != null) {
            do {
                String enumOptions = "Options: " + String.join(", ", java.util.Arrays.stream(enumList).map(Enum::name).map(name -> name.replace("_", " ")).toArray(String[]::new));
                writeln(enumOptions, listColor);
                String response = getStringInput(message, allowEmpty, messageColor);
                if(response == null || response.isEmpty()){
                    if(allowEmpty){
                        return null;
                    }
                    continue;
                }
                try {
                    Class<T> enumType = enumList[0].getDeclaringClass();
                    return T.valueOf(enumType, response.trim().toUpperCase().replace(" ", "_"));
                } catch (IllegalArgumentException ex) {
                    writeln("You must pick one of the options and type it exactly (case insensitive).", TextColor.RED);
                    writeln("");
                }
            } while (true);
        }
        return null;
    }

    /**
     * Gets a date from the user by asking for each part (Month, Day, Year)
     * @param message Message to show to the user as to what you are requesting
     * @param textColor ConsoleTextColor of the text when written
     * @return returns a java.time.LocalDate object populated with the users entered date
     */
    public static LocalDate getDateInput(String message, TextColor textColor) {
        do {
            if(message != null) {
                Console.writeln(message, textColor);
            }
            boolean getInputOnSameLineCurrent = getInputOnSameLine;
            getInputOnSameLine = true;
            int month = getIntInput("Enter month (1-12): ",1, 12, TextColor.DEFAULT);
            int day = getIntInput("Enter day (1-31): ", 1, 31, TextColor.DEFAULT);
            int year = getIntInput("Enter year: ", 1000, 9999, TextColor.DEFAULT);
            getInputOnSameLine = getInputOnSameLineCurrent;
            try {
                return LocalDate.of(year, month, day);
            } catch (DateTimeException ex) {
                Console.writeln("Invalid date, try again.", TextColor.RED);
            }
        }while(true);
    }

    /**
     * Gets a date from the user by asking for each part (Month, Day, Year)
     * Makes sure that the date typed falls between the two values provided.
     * @param message Message to show to the user as to what you are requesting
     * @param minDate The minimum date allowed
     * @param maxDate The maximum date allowed
     * @param textColor ConsoleTextColor of the text when written
     * @return returns a java.time.LocalDate object populated with the users entered date
     */
    public static LocalDate getDateInput(String message,LocalDate minDate, LocalDate maxDate, TextColor textColor) {
        do {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(defaultDateFormat);
            String betweenRequirement = String.format("between %s and %s", minDate.format(formatter), maxDate.format(formatter));
            if(message != null) {
                Console.writeln(String.format("%s (%s):", message, betweenRequirement), textColor);
            }
            boolean getInputOnSameLineCurrent = getInputOnSameLine;
            getInputOnSameLine = true;
            int month = getIntInput("Enter month (1-12): ",1, 12, TextColor.DEFAULT);
            int day = getIntInput("Enter day (1-31): ", 1, 31, TextColor.DEFAULT);
            int year = getIntInput("Enter year: ", 1000, 9999, TextColor.DEFAULT);
            getInputOnSameLine = getInputOnSameLineCurrent;
            try {
                LocalDate parsedDate = LocalDate.of(year, month, day);
                if(parsedDate.isBefore(minDate) || parsedDate.isAfter(maxDate)){
                    throw new DateTimeException("Date out of range");
                }
                return parsedDate;
            } catch (DateTimeException ex) {
                Console.writeln(String.format("Invalid date, must be %s and match the pattern. Try again.", betweenRequirement), TextColor.RED);
            }
        }while(true);
    }

    /**
     * Gets a date from the user by asking for a date that matches the defaultDateFormat of Console class.
     * @param message Message to show to the user as to what you are requesting
     * @param textColor ConsoleTextColor of the text when written
     * @return returns a java.time.LocalDate object populated with the users entered date
     */
    public static LocalDate getDateInputInline(String message, TextColor textColor) {
        do {
            String formatHelper = " [Format: \"" + defaultDateFormat + "\"]:";
            String dateSubmission = getStringInput(message + formatHelper, false, textColor);

            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(defaultDateFormat);
                return LocalDate.parse(dateSubmission, formatter);
            } catch (DateTimeException ex) {
                Console.writeln("Invalid date, try again.", TextColor.RED);
            }
        }while(true);
    }

    /**
     * Gets a date from the user expecting it to match the defaultDateFormat of Console class
     * Makes sure that the date typed falls between the minDate and maxDate.
     * @param message Message to show to the user as to what you are requesting
     * @param minDate The minimum date allowed
     * @param maxDate The maximum date allowed
     * @param textColor ConsoleTextColor of the text when written
     * @return returns a java.time.LocalDate object populated with the users entered date
     */
    public static LocalDate getDateInputInline(String message, LocalDate minDate, LocalDate maxDate, TextColor textColor){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(defaultDateFormat);
        LocalDate date;
        do {
            date = getDateInputInline(String.format("%s (between %s and %s)", message, minDate.format(formatter), maxDate.format(formatter)), textColor);
            if(date.isBefore(minDate) || date.isAfter(maxDate)){
                date = null;
                String betweenRequirement = String.format("between %s and %s", minDate.format(formatter), maxDate.format(formatter));
                Console.writeln(String.format("Invalid date, must be %s. Try again.", betweenRequirement), TextColor.RED);
            }
        }while(date == null);

        return date;
    }

    /**
     * Writes to the console using the default text color and background color.
     * Does not append the newline. Text stays on the same line.
     * @param message The message to write to the console
     */
    public static void write(String message){
        write(message, TextColor.DEFAULT, BackgroundColor.DEFAULT);
    }

    /**
     * Writes to the console using the provided text color and default background color.
     * Does not append the newline. Text stays on the same line.
     * @param message The message to write to the console
     * @param textColor The ConsoleTextColor to write the text in
     */
    public static void write(String message, TextColor textColor){
        write(message, textColor, BackgroundColor.DEFAULT);
    }

    /**
     * Writes the message to the console using the provided text and background color
     * Does not append the newline. Text stays on the same line.
     * @param message The message to write to the console
     * @param textColor The ConsoleTextColor to write the text in
     * @param backgroundColor The ConsoleBackgroundColor to place behind the text
     */
    public static void write(String message, TextColor textColor, BackgroundColor backgroundColor){
        System.out.print(getBackgroundColorText(backgroundColor) + getTextColorText(textColor) + message + RESET);
    }

    /**
     * Writes to the console using the default text color and background color.
     * Appends the newline to the end.
     * @param message The message to write to the console
     */
    public static void writeln(String message){ writeln(message, TextColor.DEFAULT, BackgroundColor.DEFAULT);}

    /**
     * Writes to the console using the provided text color and default background color.
     * Appends the newline to the end.
     * @param message The message to write to the console
     * @param textColor The ConsoleTextColor to write the text in
     */
    public static void writeln(String message, TextColor textColor) { writeln(message, textColor, BackgroundColor.DEFAULT);}

    /**
     * Writes the message to the console using the provided text and background color
     * Append the newline to the end.
     * @param message The message to write to the console
     * @param textColor The ConsoleTextColor to write the text in
     * @param backgroundColor The ConsoleBackgroundColor to place behind the text
     */
    public static void writeln(String message, TextColor textColor, BackgroundColor backgroundColor){
        System.out.println(getBackgroundColorText(backgroundColor) + getTextColorText(textColor) + message + RESET);
    }

    /**
     * Writes the string to the console using System.out.print alternating colors for each character through all the TextColor colors
     * Uses the colors from the passed in TextColors array.
     * Spaces do not cause the color to change.
     * @param message The message to write
     */
    public static <T> void writelnMultiColored(String message){
        writeMultiColored(message);
        System.out.println();
    }

    /**
     * Writes the string to the console using System.out.print alternating colors for each character through all the TextColor colors
     * Uses the colors from the passed in TextColors array.
     * Spaces do not cause the color to change.
     * @param message The message to write
     */
    public static <T> void writeMultiColored(String message){
        writeMultiColored(message, TextColor.values());
    }

    /**
     * Writes the string to the console using System.out.print alternating colors for each character
     * Uses the colors from the passed in TextColors array.
     * Spaces do not cause the color to change.
     * Pass Console.TextColor[] to the second parameter
     * @param message The message to write
     * @param colors Array of colors to cycle through. <strong>It must be either an array of Color32 or TextColor.</strong>
     */
    public static <T> void writelnMultiColored(String message, T[] colors) {
        writeMultiColored(message, colors);
        System.out.println();
    }

    /**
     * Writes the string to the console using System.out.print alternating colors for each character
     * Uses the colors from the passed in TextColors array.
     * Spaces do not cause the color to change.
     * Pass Console.TextColor[] to the second parameter
     * @param message The message to write
     * @param colors Array of colors to cycle through. <strong>It must be either an array of Color32 or TextColor.</strong>
     */
    public static <T> void writeMultiColored(String message, T[] colors) {
        if(message.isEmpty() || colors == null || colors.length == 0){
            writeln(message);
            return;
        }
        if (colors[0] instanceof Color32 || colors[0] instanceof TextColor){
            int colorIndexToUse = 0;
            for (char c : message.toCharArray()){
                if(Character.isWhitespace(c)){
                    write(String.valueOf(c));
                    continue;
                }
                if (colors[colorIndexToUse] instanceof Color32 color32){
                    write(String.valueOf(c), color32);
                } else if (colors[colorIndexToUse] instanceof TextColor textColor){
                    write(String.valueOf(c), textColor);
                }
                colorIndexToUse++;
                colorIndexToUse %= colors.length;
            }
        } else {
            throw new IllegalArgumentException("Color must be a Color32 or TextColor");
        }
    }

    //region Text Style Methods (expand)

    /**
     * Writes to the console using the provided text color and style.
     * Does not append the newline. Text stays on the same line.
     * @param message The message to write to the console
     * @param textColor The ConsoleTextColor to write the text in
     * @param style The TextStyle to apply (BOLD, ITALIC, UNDERLINE, etc.)
     */
    public static void write(String message, TextColor textColor, TextStyle style){
        System.out.print(getTextStyleText(style) + getTextColorText(textColor) + message + RESET);
    }

    /**
     * Writes to the console using the provided text color, background color, and style.
     * Does not append the newline. Text stays on the same line.
     * @param message The message to write to the console
     * @param textColor The ConsoleTextColor to write the text in
     * @param backgroundColor The ConsoleBackgroundColor to place behind the text
     * @param style The TextStyle to apply (BOLD, ITALIC, UNDERLINE, etc.)
     */
    public static void write(String message, TextColor textColor, BackgroundColor backgroundColor, TextStyle style){
        System.out.print(getTextStyleText(style) + getBackgroundColorText(backgroundColor) + getTextColorText(textColor) + message + RESET);
    }

    /**
     * Writes to the console using the provided text color and style.
     * Appends the newline to the end.
     * @param message The message to write to the console
     * @param textColor The ConsoleTextColor to write the text in
     * @param style The TextStyle to apply (BOLD, ITALIC, UNDERLINE, etc.)
     */
    public static void writeln(String message, TextColor textColor, TextStyle style){
        System.out.println(getTextStyleText(style) + getTextColorText(textColor) + message + RESET);
    }

    /**
     * Writes to the console using the provided text color, background color, and style.
     * Appends the newline to the end.
     * @param message The message to write to the console
     * @param textColor The ConsoleTextColor to write the text in
     * @param backgroundColor The ConsoleBackgroundColor to place behind the text
     * @param style The TextStyle to apply (BOLD, ITALIC, UNDERLINE, etc.)
     */
    public static void writeln(String message, TextColor textColor, BackgroundColor backgroundColor, TextStyle style){
        System.out.println(getTextStyleText(style) + getBackgroundColorText(backgroundColor) + getTextColorText(textColor) + message + RESET);
    }

    //endregion

    //region Typing Effect Methods (expand)

    /**
     * Writes the message to the console one character at a time with a delay,
     * creating a "typing" effect. Uses default text color.
     * @param message The message to write
     * @param delayMs Delay in milliseconds between each character
     */
    public static void writeTyping(String message, int delayMs){
        writeTyping(message, delayMs, TextColor.DEFAULT);
    }

    /**
     * Writes the message to the console one character at a time with a delay,
     * creating a "typing" effect.
     * @param message The message to write
     * @param delayMs Delay in milliseconds between each character
     * @param textColor The color of the text
     */
    public static void writeTyping(String message, int delayMs, TextColor textColor){
        for (char c : message.toCharArray()) {
            write(String.valueOf(c), textColor);
            pause(delayMs);
        }
    }

    /**
     * Writes the message to the console one character at a time with a delay,
     * creating a "typing" effect, then adds a newline. Uses default text color.
     * @param message The message to write
     * @param delayMs Delay in milliseconds between each character
     */
    public static void writelnTyping(String message, int delayMs){
        writeTyping(message, delayMs);
        System.out.println();
    }

    /**
     * Writes the message to the console one character at a time with a delay,
     * creating a "typing" effect, then adds a newline.
     * @param message The message to write
     * @param delayMs Delay in milliseconds between each character
     * @param textColor The color of the text
     */
    public static void writelnTyping(String message, int delayMs, TextColor textColor){
        writeTyping(message, delayMs, textColor);
        System.out.println();
    }

    /**
     * Writes the message one character at a time with rainbow colors 🌈
     * @param message The message to write
     * @param delayMs Delay in milliseconds between each character
     */
    public static void writeTypingRainbow(String message, int delayMs){
        writeTypingRainbow(message, delayMs, 0, (1.0f / message.length()) * 360);
    }

    /**
     * Writes the message one character at a time with rainbow colors 🌈
     * @param message The message to write
     * @param delayMs Delay in milliseconds between each character
     * @param startingHue Where the hue should start (0-360)
     * @param rotateHueAmount Amount to rotate the hue by each character
     */
    public static void writeTypingRainbow(String message, int delayMs, float startingHue, float rotateHueAmount){
        Color32 color = new Color32(0);
        float rotation = startingHue;
        for (char c : message.toCharArray()) {
            color.setHSLValues(rotation, 1, 0.5f);
            write(String.valueOf(c), color);
            pause(delayMs);
            rotation += rotateHueAmount;
        }
    }

    /**
     * Writes the message one character at a time with rainbow colors, then adds a newline 🌈
     * @param message The message to write
     * @param delayMs Delay in milliseconds between each character
     */
    public static void writelnTypingRainbow(String message, int delayMs){
        writeTypingRainbow(message, delayMs);
        System.out.println();
    }

    /**
     * Writes the message one character at a time, cycling through all TextColors.
     * Spaces do not cause the color to change.
     * @param message The message to write
     * @param delayMs Delay in milliseconds between each character
     */
    public static void writeTypingMultiColored(String message, int delayMs){
        writeTypingMultiColored(message, delayMs, TextColor.values());
    }

    /**
     * Writes the message one character at a time, cycling through provided colors.
     * Spaces do not cause the color to change.
     * @param message The message to write
     * @param delayMs Delay in milliseconds between each character
     * @param colors Array of colors to cycle through (TextColor[] or Color32[])
     */
    public static <T> void writeTypingMultiColored(String message, int delayMs, T[] colors){
        if(message.isEmpty() || colors == null || colors.length == 0){
            writeTyping(message, delayMs);
            return;
        }
        if (colors[0] instanceof Color32 || colors[0] instanceof TextColor){
            int colorIndexToUse = 0;
            for (char c : message.toCharArray()){
                if(Character.isWhitespace(c)){
                    write(String.valueOf(c));
                } else {
                    if (colors[colorIndexToUse] instanceof Color32 color32){
                        write(String.valueOf(c), color32);
                    } else if (colors[colorIndexToUse] instanceof TextColor textColor){
                        write(String.valueOf(c), textColor);
                    }
                    colorIndexToUse++;
                    colorIndexToUse %= colors.length;
                }
                pause(delayMs);
            }
        } else {
            throw new IllegalArgumentException("Color must be a Color32 or TextColor");
        }
    }

    /**
     * Writes the message one character at a time, cycling through all TextColors, then adds a newline.
     * @param message The message to write
     * @param delayMs Delay in milliseconds between each character
     */
    public static void writelnTypingMultiColored(String message, int delayMs){
        writeTypingMultiColored(message, delayMs);
        System.out.println();
    }

    /**
     * Writes the message one character at a time, cycling through provided colors, then adds a newline.
     * @param message The message to write
     * @param delayMs Delay in milliseconds between each character
     * @param colors Array of colors to cycle through (TextColor[] or Color32[])
     */
    public static <T> void writelnTypingMultiColored(String message, int delayMs, T[] colors){
        writeTypingMultiColored(message, delayMs, colors);
        System.out.println();
    }

    //endregion

    //region Progress Bar Methods (expand)

    /**
     * Writes a progress bar to the console.
     * Does not append a newline.
     * @param percent The percentage complete (0-100)
     * @param width The width of the progress bar in characters
     * @param fillColor The color of the filled portion
     */
    public static void writeProgressBar(int percent, int width, TextColor fillColor){
        writeProgressBar(percent, width, fillColor, TextColor.DEFAULT, true);
    }

    /**
     * Writes a progress bar to the console.
     * Does not append a newline.
     * @param percent The percentage complete (0-100)
     * @param width The width of the progress bar in characters
     * @param fillColor The color of the filled portion
     * @param showPercent Whether to display the percentage number
     */
    public static void writeProgressBar(int percent, int width, TextColor fillColor, boolean showPercent){
        writeProgressBar(percent, width, fillColor, TextColor.DEFAULT, showPercent);
    }

    /**
     * Writes a progress bar to the console.
     * Does not append a newline.
     * @param percent The percentage complete (0-100)
     * @param width The width of the progress bar in characters
     * @param fillColor The Color32 of the filled portion
     */
    public static void writeProgressBar(int percent, int width, Color32 fillColor){
        writeProgressBar(percent, width, fillColor, new Color32(TextColor.DEFAULT), true);
    }

    /**
     * Writes a progress bar to the console.
     * Does not append a newline.
     * @param percent The percentage complete (0-100)
     * @param width The width of the progress bar in characters
     * @param fillColor The Color32 of the filled portion
     * @param showPercent Whether to display the percentage number
     */
    public static void writeProgressBar(int percent, int width, Color32 fillColor, boolean showPercent){
        writeProgressBar(percent, width, fillColor, new Color32(TextColor.DEFAULT), showPercent);
    }

    /**
     * Writes a progress bar to the console with customizable fill and empty colors.
     * Does not append a newline. Great for versus/tug-of-war style displays!
     * @param percent The percentage complete (0-100)
     * @param width The width of the progress bar in characters
     * @param fillColor The color of the filled portion
     * @param emptyColor The color of the empty portion
     * @param showPercent Whether to display the percentage number
     */
    public static void writeProgressBar(int percent, int width, TextColor fillColor, TextColor emptyColor, boolean showPercent){
        percent = Math.max(0, Math.min(100, percent));
        int filled = (int)(width * (percent / 100.0));
        int empty = width - filled;

        write("[");
        write("█".repeat(filled), fillColor);
        write("█".repeat(empty), emptyColor);
        write("]");
        if(showPercent){
            write(" " + percent + "%");
        }
    }

    /**
     * Writes a progress bar to the console with customizable fill and empty colors.
     * Does not append a newline. Great for versus/tug-of-war style displays!
     * @param percent The percentage complete (0-100)
     * @param width The width of the progress bar in characters
     * @param fillColor The Color32 of the filled portion
     * @param emptyColor The Color32 of the empty portion
     * @param showPercent Whether to display the percentage number
     */
    public static void writeProgressBar(int percent, int width, Color32 fillColor, Color32 emptyColor, boolean showPercent){
        percent = Math.max(0, Math.min(100, percent));
        int filled = (int)(width * (percent / 100.0));
        int empty = width - filled;

        write("[");
        write("█".repeat(filled), fillColor);
        write("█".repeat(empty), emptyColor);
        write("]");
        if(showPercent){
            write(" " + percent + "%");
        }
    }

    /**
     * Writes a progress bar to the console, then moves to the next line.
     * @param percent The percentage complete (0-100)
     * @param width The width of the progress bar in characters
     * @param fillColor The color of the filled portion
     */
    public static void writelnProgressBar(int percent, int width, TextColor fillColor){
        writeProgressBar(percent, width, fillColor);
        System.out.println();
    }

    /**
     * Writes a progress bar to the console, then moves to the next line.
     * @param percent The percentage complete (0-100)
     * @param width The width of the progress bar in characters
     * @param fillColor The color of the filled portion
     * @param showPercent Whether to display the percentage number
     */
    public static void writelnProgressBar(int percent, int width, TextColor fillColor, boolean showPercent){
        writeProgressBar(percent, width, fillColor, showPercent);
        System.out.println();
    }

    /**
     * Writes a progress bar to the console, then moves to the next line.
     * @param percent The percentage complete (0-100)
     * @param width The width of the progress bar in characters
     * @param fillColor The Color32 of the filled portion
     */
    public static void writelnProgressBar(int percent, int width, Color32 fillColor){
        writeProgressBar(percent, width, fillColor);
        System.out.println();
    }

    /**
     * Writes a progress bar to the console, then moves to the next line.
     * @param percent The percentage complete (0-100)
     * @param width The width of the progress bar in characters
     * @param fillColor The Color32 of the filled portion
     * @param showPercent Whether to display the percentage number
     */
    public static void writelnProgressBar(int percent, int width, Color32 fillColor, boolean showPercent){
        writeProgressBar(percent, width, fillColor, showPercent);
        System.out.println();
    }

    /**
     * Writes a progress bar to the console with customizable colors, then moves to the next line.
     * Great for versus/tug-of-war style displays!
     * @param percent The percentage complete (0-100)
     * @param width The width of the progress bar in characters
     * @param fillColor The color of the filled portion
     * @param emptyColor The color of the empty portion
     * @param showPercent Whether to display the percentage number
     */
    public static void writelnProgressBar(int percent, int width, TextColor fillColor, TextColor emptyColor, boolean showPercent){
        writeProgressBar(percent, width, fillColor, emptyColor, showPercent);
        System.out.println();
    }

    /**
     * Writes a progress bar to the console with customizable colors, then moves to the next line.
     * Great for versus/tug-of-war style displays!
     * @param percent The percentage complete (0-100)
     * @param width The width of the progress bar in characters
     * @param fillColor The Color32 of the filled portion
     * @param emptyColor The Color32 of the empty portion
     * @param showPercent Whether to display the percentage number
     */
    public static void writelnProgressBar(int percent, int width, Color32 fillColor, Color32 emptyColor, boolean showPercent){
        writeProgressBar(percent, width, fillColor, emptyColor, showPercent);
        System.out.println();
    }

    //endregion

    //region Stat Bar Methods (expand)

    /**
     * Writes a stat bar to the console showing current/max values.
     * Does not append a newline. Great for health bars, XP, inventory slots, etc.
     * @param current The current value
     * @param max The maximum value
     * @param width The width of the bar in characters
     * @param fillColor The color of the filled portion
     */
    public static void writeStatBar(int current, int max, int width, TextColor fillColor){
        writeStatBar(current, max, width, fillColor, TextColor.DEFAULT, true);
    }

    /**
     * Writes a stat bar to the console showing current/max values.
     * Does not append a newline. Great for health bars, XP, inventory slots, etc.
     * @param current The current value
     * @param max The maximum value
     * @param width The width of the bar in characters
     * @param fillColor The color of the filled portion
     * @param showValues Whether to display the current/max numbers
     */
    public static void writeStatBar(int current, int max, int width, TextColor fillColor, boolean showValues){
        writeStatBar(current, max, width, fillColor, TextColor.DEFAULT, showValues);
    }

    /**
     * Writes a stat bar to the console showing current/max values.
     * Does not append a newline. Great for health bars, XP, inventory slots, etc.
     * @param current The current value
     * @param max The maximum value
     * @param width The width of the bar in characters
     * @param fillColor The Color32 of the filled portion
     */
    public static void writeStatBar(int current, int max, int width, Color32 fillColor){
        writeStatBar(current, max, width, fillColor, new Color32(TextColor.DEFAULT), true);
    }

    /**
     * Writes a stat bar to the console showing current/max values.
     * Does not append a newline. Great for health bars, XP, inventory slots, etc.
     * @param current The current value
     * @param max The maximum value
     * @param width The width of the bar in characters
     * @param fillColor The Color32 of the filled portion
     * @param showValues Whether to display the current/max numbers
     */
    public static void writeStatBar(int current, int max, int width, Color32 fillColor, boolean showValues){
        writeStatBar(current, max, width, fillColor, new Color32(TextColor.DEFAULT), showValues);
    }

    /**
     * Writes a stat bar to the console with customizable fill and empty colors.
     * Does not append a newline. Great for health bars, XP, inventory slots, etc.
     * @param current The current value
     * @param max The maximum value
     * @param width The width of the bar in characters
     * @param fillColor The color of the filled portion
     * @param emptyColor The color of the empty portion
     * @param showValues Whether to display the current/max numbers
     */
    public static void writeStatBar(int current, int max, int width, TextColor fillColor, TextColor emptyColor, boolean showValues){
        if(max <= 0) max = 1;
        current = Math.max(0, Math.min(current, max));
        int filled = (int)(width * ((double)current / max));
        int empty = width - filled;

        write("[");
        write("█".repeat(filled), fillColor);
        write("█".repeat(empty), emptyColor);
        write("]");
        if(showValues){
            write(" " + current + "/" + max);
        }
    }

    /**
     * Writes a stat bar to the console with customizable fill and empty colors.
     * Does not append a newline. Great for health bars, XP, inventory slots, etc.
     * @param current The current value
     * @param max The maximum value
     * @param width The width of the bar in characters
     * @param fillColor The Color32 of the filled portion
     * @param emptyColor The Color32 of the empty portion
     * @param showValues Whether to display the current/max numbers
     */
    public static void writeStatBar(int current, int max, int width, Color32 fillColor, Color32 emptyColor, boolean showValues){
        if(max <= 0) max = 1;
        current = Math.max(0, Math.min(current, max));
        int filled = (int)(width * ((double)current / max));
        int empty = width - filled;

        write("[");
        write("█".repeat(filled), fillColor);
        write("█".repeat(empty), emptyColor);
        write("]");
        if(showValues){
            write(" " + current + "/" + max);
        }
    }

    /**
     * Writes a stat bar to the console, then moves to the next line.
     * Great for health bars, XP, inventory slots, etc.
     * @param current The current value
     * @param max The maximum value
     * @param width The width of the bar in characters
     * @param fillColor The color of the filled portion
     */
    public static void writelnStatBar(int current, int max, int width, TextColor fillColor){
        writeStatBar(current, max, width, fillColor);
        System.out.println();
    }

    /**
     * Writes a stat bar to the console, then moves to the next line.
     * Great for health bars, XP, inventory slots, etc.
     * @param current The current value
     * @param max The maximum value
     * @param width The width of the bar in characters
     * @param fillColor The color of the filled portion
     * @param showValues Whether to display the current/max numbers
     */
    public static void writelnStatBar(int current, int max, int width, TextColor fillColor, boolean showValues){
        writeStatBar(current, max, width, fillColor, showValues);
        System.out.println();
    }

    /**
     * Writes a stat bar to the console, then moves to the next line.
     * Great for health bars, XP, inventory slots, etc.
     * @param current The current value
     * @param max The maximum value
     * @param width The width of the bar in characters
     * @param fillColor The Color32 of the filled portion
     */
    public static void writelnStatBar(int current, int max, int width, Color32 fillColor){
        writeStatBar(current, max, width, fillColor);
        System.out.println();
    }

    /**
     * Writes a stat bar to the console, then moves to the next line.
     * Great for health bars, XP, inventory slots, etc.
     * @param current The current value
     * @param max The maximum value
     * @param width The width of the bar in characters
     * @param fillColor The Color32 of the filled portion
     * @param showValues Whether to display the current/max numbers
     */
    public static void writelnStatBar(int current, int max, int width, Color32 fillColor, boolean showValues){
        writeStatBar(current, max, width, fillColor, showValues);
        System.out.println();
    }

    /**
     * Writes a stat bar to the console with customizable colors, then moves to the next line.
     * Great for health bars, XP, inventory slots, etc.
     * @param current The current value
     * @param max The maximum value
     * @param width The width of the bar in characters
     * @param fillColor The color of the filled portion
     * @param emptyColor The color of the empty portion
     * @param showValues Whether to display the current/max numbers
     */
    public static void writelnStatBar(int current, int max, int width, TextColor fillColor, TextColor emptyColor, boolean showValues){
        writeStatBar(current, max, width, fillColor, emptyColor, showValues);
        System.out.println();
    }

    /**
     * Writes a stat bar to the console with customizable colors, then moves to the next line.
     * Great for health bars, XP, inventory slots, etc.
     * @param current The current value
     * @param max The maximum value
     * @param width The width of the bar in characters
     * @param fillColor The Color32 of the filled portion
     * @param emptyColor The Color32 of the empty portion
     * @param showValues Whether to display the current/max numbers
     */
    public static void writelnStatBar(int current, int max, int width, Color32 fillColor, Color32 emptyColor, boolean showValues){
        writeStatBar(current, max, width, fillColor, emptyColor, showValues);
        System.out.println();
    }

    //endregion

    //region writelnCustoms (expand)
    public static void writelnYellow(String text){
        System.out.println(Console.YELLOW + text + RESET);
    }
    public static void writelnBlue(String text){
        System.out.println(Console.BLUE + text + RESET);
    }
    public static void writelnRed(String text){
        System.out.println(Console.RED + text + RESET);
    }
    public static void writelnGreen(String text){
        System.out.println(Console.GREEN + text + RESET);
    }
    public static void writelnPurple(String text){
        System.out.println(Console.PURPLE + text + RESET);
    }
    public static void writelnCyan(String text){
        System.out.println(Console.CYAN + text + RESET);
    }
    public static void writelnWhite(String text){
        System.out.println(Console.WHITE + text + RESET);
    }
    public static void writelnBlack(String text){
        System.out.println(Console.BLACK + text + RESET);
    }

    public static void writeYellow(String text){
        System.out.print(Console.YELLOW + text + RESET);
    }
    public static void writeBlue(String text){
        System.out.print(Console.BLUE + text + RESET);
    }
    public static void writeRed(String text){
        System.out.print(Console.RED + text + RESET);
    }
    public static void writeGreen(String text){
        System.out.print(Console.GREEN + text + RESET);
    }
    public static void writePurple(String text){
        System.out.print(Console.PURPLE + text + RESET);
    }
    public static void writeCyan(String text){
        System.out.print(Console.CYAN + text + RESET);
    }
    public static void writeWhite(String text){
        System.out.print(Console.WHITE + text + RESET);
    }
    public static void writeBlack(String text){
        System.out.print(Console.BLACK + text + RESET);
    }

    //endregion

    /**
     * Clears the console screen.
     * Uses ANSI escape codes if FULL_ANSI_COLOR_SUPPORT is enabled,
     * otherwise falls back to printing empty lines.
     */
    public static void clear(){
        if (FULL_ANSI_COLOR_SUPPORT) {
            System.out.print("\u001B[2J\u001B[H");
            System.out.flush();
        } else {
            clear(50);
        }
    }

    /**
     * "Clears" the console by writing empty lines (fallback method)
     * @param emptyLineCount The number of empty lines to print
     */
    public static void clear(int emptyLineCount){
        for (int i = 0; i < emptyLineCount; i++) {
            System.out.println();
        }
    }

    /**
     * Pauses execution for the specified number of milliseconds.
     * Useful for creating dramatic pauses or timing effects.
     * @param milliseconds The number of milliseconds to pause
     */
    public static void pause(int milliseconds){
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Pauses execution and waits for the user to press Enter.
     * Displays the provided message before waiting.
     * @param message The message to display (e.g., "Press Enter to continue...")
     */
    public static void waitForEnter(String message){
        write(message);
        try {
            br.readLine();
        } catch (IOException ignored) {}
    }

    /**
     * Pauses execution and waits for the user to press Enter.
     * Uses a default message: "Press Enter to continue..."
     */
    public static void waitForEnter(){
        waitForEnter("Press Enter to continue...");
    }


    /**
     * Prints a formatted table to the console with auto-sized columns
     * and a default padding of 3 spaces between columns.
     *
     * @param data a 2D String array where data[0] is the header row
     *             and all subsequent rows are data rows
     */
    public static void printTable(String[][] data) {
        printTable(data, 3);
    }

    /**
     * Prints a formatted table to the console with auto-sized columns.
     * Column widths are determined by the longest value in each column.
     * A divider row of dashes is printed between the header and data rows.
     *
     * <p>Example usage:</p>
     * <pre>
     * String[][] data = {
     *     {"Name", "Age", "City"},
     *     {"Alice", "30", "Denver"},
     *     {"Bob", "25", "Salt Lake City"}
     * };
     * printTable(data, 3);
     * </pre>
     *
     * <p>Output:</p>
     * <pre>
     * Name    Age   City
     * ----    ---   ----
     * Alice   30    Denver
     * Bob     25    Salt Lake City
     * </pre>
     *
     * @param data    a 2D String array where data[0] is the header row
     *                and all subsequent rows are data rows.
     *                All rows should have the same number of columns.
     * @param padding the number of extra spaces added after each column's
     *                widest value to create spacing between columns
     */
    public static void printTable(String[][] data, int padding) {
        if (data == null || data.length == 0) {
            return;
        }

        int columns = data[0].length;

        // find the widest value in each column
        int[] columnWidths = new int[columns];
        for (String[] row : data) {
            for (int col = 0; col < row.length; col++) {
                if (row[col].length() > columnWidths[col]) {
                    columnWidths[col] = row[col].length();
                }
            }
        }

        // print header row
        StringBuilder headerRow = new StringBuilder();
        StringBuilder dividerRow = new StringBuilder();
        for (int col = 0; col < columns; col++) {
            int width = columnWidths[col] + padding;
            headerRow.append(data[0][col]);
            dividerRow.append("-".repeat(columnWidths[col]));
            if (col < columns - 1) {
                headerRow.append(" ".repeat(width - data[0][col].length()));
                dividerRow.append(" ".repeat(width - columnWidths[col]));
            }
        }
        System.out.println(headerRow);
        System.out.println(dividerRow);

        // print data rows
        for (int row = 1; row < data.length; row++) {
            StringBuilder dataRow = new StringBuilder();
            for (int col = 0; col < data[row].length; col++) {
                int width = columnWidths[col] + padding;
                dataRow.append(data[row][col]);
                if (col < columns - 1) {
                    dataRow.append(" ".repeat(width - data[row][col].length()));
                }
            }
            System.out.println(dataRow);
        }
    }

    // ============================================================
// Grid Display & Input Methods for Console Library
// ============================================================
// Add these methods to your existing Console class.
// Dependencies: Console.write, Console.writeln,
//               Console.getStringInput, TextColor, br
// ============================================================


// ────────────────────────────────────────────────────────────
//  Display Width Helper (private)
// ────────────────────────────────────────────────────────────

    /**
     * Estimates the terminal display width of a string by accounting
     * for characters that occupy two columns in a monospace terminal.
     * This includes most emoji (supplementary Unicode), CJK characters,
     * Hangul, and fullwidth forms.
     *
     * <p>Standard ASCII and most Latin/Cyrillic/Greek characters are
     * counted as 1 column each. This is an estimation — actual rendering
     * may vary by terminal emulator, font, and OS.</p>
     *
     * @param s the string to measure
     * @return the estimated number of terminal columns the string occupies
     */
    private static int displayWidth(String s) {
        if (s == null) return 0;
        int width = 0;
        for (int i = 0; i < s.length(); ) {
            int cp = s.codePointAt(i);
            i += Character.charCount(cp);

            // Zero Width Joiner and Variation Selectors add no terminal width
            if (cp == 0x200D || (cp >= 0xFE00 && cp <= 0xFE0F)) {
                continue;
            }

            if (cp > 0xFFFF                          // supplementary plane (most emoji)
                    || (cp >= 0x1100 && cp <= 0x115F) // Hangul Jamo
                    || (cp >= 0x2600 && cp <= 0x27BF) // Misc Symbols & Dingbats
                    || (cp >= 0x2E80 && cp <= 0x9FFF) // CJK Unified + Radicals
                    || (cp >= 0xAC00 && cp <= 0xD7AF) // Hangul Syllables
                    || (cp >= 0xF900 && cp <= 0xFAFF) // CJK Compatibility Ideographs
                    || (cp >= 0xFE10 && cp <= 0xFE6F) // CJK Compatibility Forms
                    || (cp >= 0xFF01 && cp <= 0xFF60) // Fullwidth Forms
                    || (cp >= 0xFFE0 && cp <= 0xFFE6)) { // Fullwidth Signs
                width += 2;
            } else {
                width += 1;
            }
        }
        return width;
    }


// ────────────────────────────────────────────────────────────
//  Selection Character Mapping (private helpers)
// ────────────────────────────────────────────────────────────

    /**
     * Character pool used for grid selection keys.
     * Order: lowercase a-z, uppercase A-Z, digits 0-9, then symbols.
     * Supports up to 84 unique cells.
     */
    private static final String SELECTION_CHARS =
            "abcdefghijklmnopqrstuvwxyz"
                    + "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                    + "0123456789"
                    + "!@#$%^&*+=?~<>{}[]|";

    /**
     * Returns the single-character selection key for a given cell index.
     *
     * @param index the 0-based cell index (row-major order)
     * @return the character representing that cell
     * @throws IllegalArgumentException if index exceeds available characters
     */
    private static char getSelectionChar(int index) {
        if (index < 0 || index >= SELECTION_CHARS.length()) {
            throw new IllegalArgumentException(
                    "Grid too large for single-character selection. Max cells: "
                            + SELECTION_CHARS.length());
        }
        return SELECTION_CHARS.charAt(index);
    }

    /**
     * Returns the cell index for a given selection character,
     * or -1 if the character is not a valid selection key.
     *
     * @param c the character the user typed
     * @return the 0-based cell index, or -1 if invalid
     */
    private static int getSelectionIndex(char c) {
        return SELECTION_CHARS.indexOf(c);
    }


// ────────────────────────────────────────────────────────────
//  Highlight border color resolution (private helpers)
// ────────────────────────────────────────────────────────────

    /**
     * Determines the color for a border segment based on which
     * adjacent cells (if any) are highlighted.
     *
     * @param highlightMap rows×cols array of highlight colors (null = no highlight)
     * @param rows         total grid rows
     * @param cols         total grid columns
     * @param adjRows      row indices of adjacent cells to check
     * @param adjCols      column indices of adjacent cells to check
     * @param defaultColor the fallback border color
     * @return the resolved color for this border segment
     */
    private static TextColor resolveSegmentColor(TextColor[][] highlightMap,
                                                 int rows, int cols,
                                                 int[] adjRows, int[] adjCols,
                                                 TextColor defaultColor) {
        if (highlightMap == null) {
            return defaultColor;
        }
        for (int i = 0; i < adjRows.length; i++) {
            int r = adjRows[i];
            int c = adjCols[i];
            if (r >= 0 && r < rows && c >= 0 && c < cols
                    && highlightMap[r][c] != null) {
                return highlightMap[r][c];
            }
        }
        return defaultColor;
    }

    /**
     * Builds a highlight map from parallel arrays of cell positions
     * and colors.
     *
     * @param rows            grid row count
     * @param cols            grid column count
     * @param highlightCells  array of {row, col} positions (0-based)
     * @param highlightColors parallel array of colors for each position
     * @return a rows×cols TextColor array (null = not highlighted)
     */
    private static TextColor[][] buildHighlightMap(int rows, int cols,
                                                   int[][] highlightCells,
                                                   TextColor[] highlightColors) {
        if (highlightCells == null || highlightColors == null
                || highlightCells.length == 0) {
            return null;
        }
        TextColor[][] map = new TextColor[rows][cols];
        for (int i = 0; i < highlightCells.length; i++) {
            int r = highlightCells[i][0];
            int c = highlightCells[i][1];
            if (r >= 0 && r < rows && c >= 0 && c < cols
                    && i < highlightColors.length) {
                map[r][c] = highlightColors[i];
            }
        }
        return map;
    }


// ────────────────────────────────────────────────────────────
//  printGrid overloads
// ────────────────────────────────────────────────────────────

    /**
     * Prints a formatted grid with borders, no labels, default colors.
     *
     * @param grid a 2D String array representing the grid contents
     */
    public static void printGrid(String[][] grid) {
        printGrid(grid, false, true, 1, 0,
                TextColor.DEFAULT, TextColor.DEFAULT, TextColor.DEFAULT,
                null, null);
    }

    /**
     * Prints a formatted grid with borders and specified cell padding.
     *
     * @param grid        a 2D String array representing the grid contents
     * @param cellPadding spaces on each side of the cell content
     */
    public static void printGrid(String[][] grid, int cellPadding) {
        printGrid(grid, false, true, cellPadding, 0,
                TextColor.DEFAULT, TextColor.DEFAULT, TextColor.DEFAULT,
                null, null);
    }

    /**
     * Prints a formatted grid with optional labels. Shows borders by default.
     *
     * @param grid       a 2D String array representing the grid contents
     * @param showLabels if true, displays column letters and row numbers
     */
    public static void printGrid(String[][] grid, boolean showLabels) {
        printGrid(grid, showLabels, true, 1, 0,
                TextColor.DEFAULT, TextColor.DEFAULT, TextColor.DEFAULT,
                null, null);
    }

    /**
     * Prints a formatted grid with optional labels and specified cell padding.
     * Shows borders by default.
     *
     * @param grid        a 2D String array representing the grid contents
     * @param showLabels  if true, displays column letters and row numbers
     * @param cellPadding spaces on each side of the cell content
     */
    public static void printGrid(String[][] grid, boolean showLabels, int cellPadding) {
        printGrid(grid, showLabels, true, cellPadding, 0,
                TextColor.DEFAULT, TextColor.DEFAULT, TextColor.DEFAULT,
                null, null);
    }

    /**
     * Prints a formatted grid with optional labels and borders.
     *
     * @param grid        a 2D String array representing the grid contents
     * @param showLabels  if true, displays column letters and row numbers
     * @param showBorders if true, draws box borders around each cell
     */
    public static void printGrid(String[][] grid, boolean showLabels, boolean showBorders) {
        printGrid(grid, showLabels, showBorders, 1, 0,
                TextColor.DEFAULT, TextColor.DEFAULT, TextColor.DEFAULT,
                null, null);
    }

    /**
     * Prints a formatted grid with optional labels, borders, and padding.
     *
     * @param grid        a 2D String array representing the grid contents
     * @param showLabels  if true, displays column letters and row numbers
     * @param showBorders if true, draws box borders around each cell
     * @param cellPadding spaces on each side of the cell content
     */
    public static void printGrid(String[][] grid, boolean showLabels,
                                 boolean showBorders, int cellPadding) {
        printGrid(grid, showLabels, showBorders, cellPadding, 0,
                TextColor.DEFAULT, TextColor.DEFAULT, TextColor.DEFAULT,
                null, null);
    }

    /**
     * Prints a formatted grid with all sizing options. Default colors.
     *
     * @param grid         a 2D String array representing the grid contents
     * @param showLabels   if true, displays column letters and row numbers
     * @param showBorders  if true, draws box borders around each cell
     * @param cellPadding  spaces on each side of the cell content
     * @param minCellWidth minimum character width for cell content area
     */
    public static void printGrid(String[][] grid, boolean showLabels,
                                 boolean showBorders, int cellPadding,
                                 int minCellWidth) {
        printGrid(grid, showLabels, showBorders, cellPadding, minCellWidth,
                TextColor.DEFAULT, TextColor.DEFAULT, TextColor.DEFAULT,
                null, null);
    }

    /**
     * Prints a formatted grid with all sizing and color options,
     * but no cell highlighting.
     *
     * @param grid         a 2D String array representing the grid contents
     * @param showLabels   if true, displays column letters and row numbers
     * @param showBorders  if true, draws box borders around each cell
     * @param cellPadding  spaces on each side of the cell content
     * @param minCellWidth minimum character width for cell content area
     * @param borderColor  color for border characters (+, -, |)
     * @param labelColor   color for column letters and row numbers
     * @param contentColor color for cell values
     */
    public static void printGrid(String[][] grid, boolean showLabels,
                                 boolean showBorders, int cellPadding,
                                 int minCellWidth, TextColor borderColor,
                                 TextColor labelColor, TextColor contentColor) {
        printGrid(grid, showLabels, showBorders, cellPadding, minCellWidth,
                borderColor, labelColor, contentColor, null, null);
    }

    /**
     * Prints a formatted grid with highlighted cells. Default sizing and colors.
     *
     * @param grid            a 2D String array representing the grid contents
     * @param showLabels      if true, displays column letters and row numbers
     * @param highlightCells  array of {row, col} positions to highlight (0-based)
     * @param highlightColors parallel array of colors for each highlighted cell
     */
    public static void printGrid(String[][] grid, boolean showLabels,
                                 int[][] highlightCells,
                                 TextColor[] highlightColors) {
        printGrid(grid, showLabels, true, 1, 0,
                TextColor.DEFAULT, TextColor.DEFAULT, TextColor.DEFAULT,
                highlightCells, highlightColors);
    }

    /**
     * Prints a formatted grid to the console with full control over
     * labels, borders, cell sizing, colors, and per-cell highlighting.
     *
     * <p>Cell widths are auto-sized based on the widest value in each column
     * (using terminal display width, so emoji and CJK characters are measured
     * correctly), but will never be smaller than {@code minCellWidth}.</p>
     *
     * <p>When {@code highlightCells} and {@code highlightColors} are provided,
     * both the borders and the text of each highlighted cell are drawn in
     * that cell's highlight color. Shared borders between two highlighted
     * cells use the color of the first match in the array.</p>
     *
     * <p>Example with labels, highlights, and colors:</p>
     * <pre>
     * String[][] board = {
     *     {".", ".", ".", "."},
     *     {".", "K", ".", "."},
     *     {".", ".", ".", "."},
     *     {".", ".", "R", "."}
     * };
     * int[][] highlights = {{1, 1}, {3, 2}};
     * TextColor[] colors = {TextColor.YELLOW, TextColor.RED};
     *
     * printGrid(board, true, true, 1, 0,
     *           TextColor.WHITE, TextColor.CYAN, TextColor.GREEN,
     *           highlights, colors);
     * </pre>
     *
     * <p>The borders and text of cell [1,1] ("K") render in YELLOW,
     * the borders and text of cell [3,2] ("R") render in RED,
     * and all other borders/text use default colors.</p>
     *
     * @param grid            a 2D String array representing the grid contents.
     *                        Null or empty cells display as blank spaces.
     * @param showLabels      if true, displays column letters and row numbers
     * @param showBorders     if true, draws +---+ style borders around cells
     * @param cellPadding     spaces on each side of the cell content (minimum 0)
     * @param minCellWidth    minimum character width for cell content (0 = auto)
     * @param borderColor     default color for border characters (+, -, |)
     * @param labelColor      color for column letters and row numbers
     * @param contentColor    color for cell values
     * @param highlightCells  array of {row, col} positions to highlight (0-based),
     *                        or null for no highlighting
     * @param highlightColors parallel array of colors for each highlighted cell,
     *                        or null. Must be same length as highlightCells.
     */
    public static void printGrid(String[][] grid, boolean showLabels,
                                 boolean showBorders, int cellPadding,
                                 int minCellWidth, TextColor borderColor,
                                 TextColor labelColor, TextColor contentColor,
                                 int[][] highlightCells,
                                 TextColor[] highlightColors) {
        if (grid == null || grid.length == 0) {
            return;
        }
        if (cellPadding < 0) cellPadding = 0;
        if (minCellWidth < 0) minCellWidth = 0;

        int rows = grid.length;
        int cols = 0;
        for (String[] row : grid) {
            if (row != null && row.length > cols) {
                cols = row.length;
            }
        }
        if (cols == 0) return;

        // find the widest display value in each column, enforcing minimum
        int[] colWidths = new int[cols];
        for (String[] row : grid) {
            if (row == null) continue;
            for (int c = 0; c < row.length; c++) {
                String val = row[c] != null ? row[c] : "";
                int w = displayWidth(val);
                if (w > colWidths[c]) {
                    colWidths[c] = w;
                }
            }
        }
        for (int c = 0; c < cols; c++) {
            if (colWidths[c] < minCellWidth) {
                colWidths[c] = minCellWidth;
            }
            if (colWidths[c] < 1) {
                colWidths[c] = 1;
            }
        }

        TextColor[][] highlightMap = buildHighlightMap(rows, cols,
                highlightCells, highlightColors);

        printGridInternal(grid, rows, cols, colWidths, showLabels,
                showBorders, cellPadding, borderColor, labelColor,
                contentColor, highlightMap);
    }


// ────────────────────────────────────────────────────────────
//  Internal grid renderer
// ────────────────────────────────────────────────────────────

    /**
     * Internal method that handles all grid rendering using Console.write
     * for per-segment color control. Uses displayWidth() for accurate
     * terminal column measurement.
     */
    private static void printGridInternal(String[][] grid, int rows, int cols,
                                          int[] colWidths, boolean showLabels,
                                          boolean showBorders, int cellPadding,
                                          TextColor borderColor,
                                          TextColor labelColor,
                                          TextColor contentColor,
                                          TextColor[][] highlightMap) {
        int gutterWidth = 0;
        String rowLabelFormat = "";
        if (showLabels) {
            gutterWidth = String.valueOf(rows).length() + 1;
            rowLabelFormat = "%" + (gutterWidth - 1) + "d ";
        }

        // ── column header line ──
        if (showLabels) {
            write(" ".repeat(gutterWidth), labelColor);
            if (showBorders) {
                write(" ", labelColor);
            }
            for (int c = 0; c < cols; c++) {
                int cellWidth = colWidths[c] + (cellPadding * 2);
                String label = String.valueOf((char) ('A' + c));
                int labelPad = (cellWidth - label.length()) / 2;
                write(" ".repeat(labelPad), labelColor);
                write(label, labelColor);
                write(" ".repeat(cellWidth - labelPad - label.length()), labelColor);
                if (showBorders && c < cols - 1) {
                    write(" ", labelColor);
                }
            }
            writeln("");
        }

        // ── print each row ──
        for (int r = 0; r < rows; r++) {
            if (showBorders) {
                printGridDivider(r, rows, cols, colWidths, cellPadding,
                        gutterWidth, showLabels, borderColor, highlightMap);
            }

            if (showLabels) {
                write(String.format(rowLabelFormat, r + 1), labelColor);
            }

            if (showBorders) {
                TextColor leftColor = resolveSegmentColor(highlightMap, rows, cols,
                        new int[]{r}, new int[]{0}, borderColor);
                write("|", leftColor);
            }

            for (int c = 0; c < cols; c++) {
                String val = "";
                if (grid[r] != null && c < grid[r].length && grid[r][c] != null) {
                    val = grid[r][c];
                }

                int contentWidth = colWidths[c];
                int valDisplayWidth = displayWidth(val);

                // resolve cell text color (highlighted cells use highlight color)
                TextColor cellColor = contentColor;
                if (highlightMap != null && highlightMap[r][c] != null) {
                    cellColor = highlightMap[r][c];
                }

                if (showBorders) {
                    int totalCell = contentWidth + (cellPadding * 2);
                    int contentPad = (totalCell - valDisplayWidth) / 2;
                    write(" ".repeat(contentPad), cellColor);
                    write(val, cellColor);
                    write(" ".repeat(totalCell - contentPad - valDisplayWidth), cellColor);

                    // trailing |
                    TextColor pipeColor;
                    if (c < cols - 1) {
                        pipeColor = resolveSegmentColor(highlightMap, rows, cols,
                                new int[]{r, r}, new int[]{c, c + 1}, borderColor);
                    } else {
                        pipeColor = resolveSegmentColor(highlightMap, rows, cols,
                                new int[]{r}, new int[]{c}, borderColor);
                    }
                    write("|", pipeColor);
                } else {
                    write(" ".repeat(cellPadding), cellColor);
                    write(val, cellColor);
                    write(" ".repeat(contentWidth - valDisplayWidth + cellPadding), cellColor);
                }
            }

            writeln("");
        }

        if (showBorders) {
            printGridDivider(rows, rows, cols, colWidths, cellPadding,
                    gutterWidth, showLabels, borderColor, highlightMap);
        }
    }

    /**
     * Prints a single grid divider line with per-segment highlight coloring.
     */
    private static void printGridDivider(int divRow, int rows, int cols,
                                         int[] colWidths, int cellPadding,
                                         int gutterWidth, boolean showLabels,
                                         TextColor borderColor,
                                         TextColor[][] highlightMap) {
        if (showLabels) {
            write(" ".repeat(gutterWidth), borderColor);
        }

        for (int c = 0; c < cols; c++) {
            TextColor plusColor = resolveSegmentColor(highlightMap, rows, cols,
                    new int[]{divRow - 1, divRow - 1, divRow, divRow},
                    new int[]{c - 1, c, c - 1, c},
                    borderColor);
            write("+", plusColor);

            TextColor dashColor = resolveSegmentColor(highlightMap, rows, cols,
                    new int[]{divRow - 1, divRow},
                    new int[]{c, c},
                    borderColor);
            write("-".repeat(colWidths[c] + (cellPadding * 2)), dashColor);
        }

        TextColor lastPlusColor = resolveSegmentColor(highlightMap, rows, cols,
                new int[]{divRow - 1, divRow},
                new int[]{cols - 1, cols - 1},
                borderColor);
        write("+", lastPlusColor);

        writeln("");
    }


// ────────────────────────────────────────────────────────────
//  getGridCoordinate — label/index based typed input
// ────────────────────────────────────────────────────────────

    /**
     * Prompts for a grid coordinate using label format (e.g. "1,A").
     *
     * @param message the prompt message
     * @param rows    number of rows in the grid
     * @param cols    number of columns in the grid
     * @return int array {rowIndex, colIndex} (0-based)
     */
    public static int[] getGridCoordinate(String message, int rows, int cols) {
        return getGridCoordinate(message, rows, cols, true, TextColor.WHITE);
    }

    /**
     * Prompts for a grid coordinate with configurable label mode.
     *
     * @param message   the prompt message
     * @param rows      number of rows
     * @param cols      number of columns
     * @param useLabels true for "1,A" format; false for "0,0" format
     * @return int array {rowIndex, colIndex} (0-based)
     */
    public static int[] getGridCoordinate(String message, int rows, int cols,
                                          boolean useLabels) {
        return getGridCoordinate(message, rows, cols, useLabels, TextColor.WHITE);
    }

    /**
     * Prompts the user for a grid coordinate and returns 0-based array indices.
     * Re-prompts until valid input is provided.
     *
     * <p>When useLabels is true, accepts flexible input formats:</p>
     * <ul>
     *   <li>"1,A" or "1, A" — row number then column letter</li>
     *   <li>"A1" or "A,1" — column letter then row number</li>
     * </ul>
     *
     * <p>When useLabels is false, accepts "row,col" using 0-based indices.</p>
     *
     * @param message   the prompt message
     * @param rows      number of rows (must be &gt; 0)
     * @param cols      number of columns (must be &gt; 0, max 26 with labels)
     * @param useLabels true for label format; false for numeric indices
     * @param textColor color of the prompt text
     * @return int array {rowIndex, colIndex} (0-based)
     */
    public static int[] getGridCoordinate(String message, int rows, int cols,
                                          boolean useLabels, TextColor textColor) {
        while (true) {
            String input = getStringInput(message, false, textColor).trim().toUpperCase();

            int rowIndex = -1;
            int colIndex = -1;

            if (useLabels) {
                if (input.contains(",")) {
                    String[] parts = input.split(",");
                    if (parts.length == 2) {
                        String left = parts[0].trim();
                        String right = parts[1].trim();

                        if (left.length() == 1 && Character.isLetter(left.charAt(0))) {
                            colIndex = left.charAt(0) - 'A';
                            try { rowIndex = Integer.parseInt(right) - 1; }
                            catch (NumberFormatException ignored) {}
                        } else if (right.length() == 1 && Character.isLetter(right.charAt(0))) {
                            colIndex = right.charAt(0) - 'A';
                            try { rowIndex = Integer.parseInt(left) - 1; }
                            catch (NumberFormatException ignored) {}
                        }
                    }
                } else if (input.length() >= 2) {
                    if (Character.isLetter(input.charAt(0))) {
                        colIndex = input.charAt(0) - 'A';
                        try { rowIndex = Integer.parseInt(input.substring(1)) - 1; }
                        catch (NumberFormatException ignored) {}
                    } else if (Character.isLetter(input.charAt(input.length() - 1))) {
                        colIndex = input.charAt(input.length() - 1) - 'A';
                        try { rowIndex = Integer.parseInt(input.substring(0, input.length() - 1)) - 1; }
                        catch (NumberFormatException ignored) {}
                    }
                }
            } else {
                if (input.contains(",")) {
                    String[] parts = input.split(",");
                    if (parts.length == 2) {
                        try {
                            rowIndex = Integer.parseInt(parts[0].trim());
                            colIndex = Integer.parseInt(parts[1].trim());
                        } catch (NumberFormatException ignored) {}
                    }
                }
            }

            if (rowIndex >= 0 && rowIndex < rows && colIndex >= 0 && colIndex < cols) {
                return new int[]{rowIndex, colIndex};
            }

            if (useLabels) {
                char maxCol = (char) ('A' + cols - 1);
                writeln("Invalid coordinate. Use format like 1,A. "
                        + "Row: 1-" + rows + ", Column: A-" + maxCol, TextColor.RED);
            } else {
                writeln("Invalid coordinate. Use format like 0,0. "
                        + "Row: 0-" + (rows - 1) + ", Column: 0-" + (cols - 1), TextColor.RED);
            }
        }
    }


// ────────────────────────────────────────────────────────────
//  getGridSelection — single-character pick from visual grid
// ────────────────────────────────────────────────────────────

    /**
     * Displays a selection grid and prompts the user to pick a cell
     * by typing its single-character key. Returns 0-based coordinates.
     *
     * @param message the prompt message
     * @param rows    number of rows in the grid
     * @param cols    number of columns in the grid
     * @return int array {rowIndex, colIndex} (0-based)
     */
    public static int[] getGridSelection(String message, int rows, int cols) {
        return getGridSelection(message, rows, cols, 1, 0,
                TextColor.WHITE, TextColor.DEFAULT, TextColor.DEFAULT);
    }

    /**
     * Displays a selection grid with configurable sizing.
     *
     * @param message      the prompt message
     * @param rows         number of rows
     * @param cols         number of columns
     * @param cellPadding  spaces on each side of cell content
     * @param minCellWidth minimum character width for cell content area
     * @return int array {rowIndex, colIndex} (0-based)
     */
    public static int[] getGridSelection(String message, int rows, int cols,
                                         int cellPadding, int minCellWidth) {
        return getGridSelection(message, rows, cols, cellPadding, minCellWidth,
                TextColor.WHITE, TextColor.DEFAULT, TextColor.DEFAULT);
    }

    /**
     * Displays a selection grid with full control over sizing and colors.
     *
     * @param message        the prompt message
     * @param rows           number of rows (must be &gt; 0)
     * @param cols           number of columns (must be &gt; 0)
     * @param cellPadding    spaces on each side of cell content
     * @param minCellWidth   minimum character width for cell content area
     * @param promptColor    color of the prompt text
     * @param borderColor    color for the selection grid borders
     * @param selectionColor color for the selection characters
     * @return int array {rowIndex, colIndex} (0-based)
     * @throws IllegalArgumentException if rows × cols exceeds 84
     */
    public static int[] getGridSelection(String message, int rows, int cols,
                                         int cellPadding, int minCellWidth,
                                         TextColor promptColor,
                                         TextColor borderColor,
                                         TextColor selectionColor) {
        int totalCells = rows * cols;
        if (totalCells > SELECTION_CHARS.length()) {
            throw new IllegalArgumentException(
                    "Grid has " + totalCells + " cells but only "
                            + SELECTION_CHARS.length() + " selection characters are available.");
        }

        String[][] selectionGrid = new String[rows][cols];
        int index = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                selectionGrid[r][c] = String.valueOf(getSelectionChar(index));
                index++;
            }
        }

        int contentWidth = Math.max(1, minCellWidth);
        int[] colWidths = new int[cols];

        Arrays.fill(colWidths, contentWidth);

        printGridInternal(selectionGrid, rows, cols, colWidths,
                false, true, Math.max(cellPadding, 0),
                borderColor, TextColor.DEFAULT, selectionColor, null);

        return promptForSelectionChar(message, totalCells, cols, promptColor);
    }


// ────────────────────────────────────────────────────────────
//  getGridSelection with existing board content
// ────────────────────────────────────────────────────────────

    /**
     * Displays the board then a selection grid beneath it. Default colors.
     *
     * @param message      the prompt message
     * @param board        the current grid contents to display
     * @param showLabels   if true, shows labels on the board
     * @param cellPadding  spaces on each side of cell content
     * @param minCellWidth minimum character width for cell content
     * @return int array {rowIndex, colIndex} (0-based)
     */
    public static int[] getGridSelection(String message, String[][] board,
                                         boolean showLabels, int cellPadding,
                                         int minCellWidth) {
        return getGridSelection(message, board, showLabels, cellPadding, minCellWidth,
                TextColor.WHITE, TextColor.DEFAULT, TextColor.DEFAULT,
                TextColor.DEFAULT, TextColor.DEFAULT);
    }

    /**
     * Displays the board with full color control, then a selection grid,
     * then prompts the user to pick a cell.
     *
     * @param message          the prompt message
     * @param board            the current grid contents to display
     * @param showLabels       if true, shows labels on the board
     * @param cellPadding      spaces on each side of cell content
     * @param minCellWidth     minimum character width for cell content
     * @param promptColor      color of the prompt text
     * @param boardBorderColor color for borders on board and selection grid
     * @param labelColor       color for column letters and row numbers
     * @param contentColor     color for cell values on the board
     * @param selectionColor   color for the selection character keys
     * @return int array {rowIndex, colIndex} (0-based)
     */
    public static int[] getGridSelection(String message, String[][] board,
                                         boolean showLabels, int cellPadding,
                                         int minCellWidth,
                                         TextColor promptColor,
                                         TextColor boardBorderColor,
                                         TextColor labelColor,
                                         TextColor contentColor,
                                         TextColor selectionColor) {
        printGrid(board, showLabels, true, cellPadding, minCellWidth,
                boardBorderColor, labelColor, contentColor, null, null);
        writeln("");

        int rows = board.length;
        int cols = 0;
        for (String[] row : board) {
            if (row != null && row.length > cols) {
                cols = row.length;
            }
        }

        return getGridSelection(message, rows, cols, cellPadding, minCellWidth,
                promptColor, boardBorderColor, selectionColor);
    }


// ────────────────────────────────────────────────────────────
//  Shared selection input prompt (private helper)
// ────────────────────────────────────────────────────────────

    /**
     * Prompts for and validates a single selection character.
     * Re-prompts until the user types a valid key within the grid.
     *
     * @param message    the prompt message
     * @param totalCells the total number of cells in the grid
     * @param cols       number of columns (to calculate row/col from index)
     * @param textColor  color of the prompt text
     * @return int array {rowIndex, colIndex} (0-based)
     */
    private static int[] promptForSelectionChar(String message, int totalCells,
                                                int cols, TextColor textColor) {
        while (true) {
            String input = getStringInput(message, false, textColor).trim();

            if (input.length() != 1) {
                writeln("Please type a single character from the grid.", TextColor.RED);
                continue;
            }

            char selected = input.charAt(0);
            int selIndex = getSelectionIndex(selected);

            if (selIndex < 0 || selIndex >= totalCells) {
                writeln("'" + selected + "' is not on the grid. Try again.", TextColor.RED);
                continue;
            }

            int rowIndex = selIndex / cols;
            int colIndex = selIndex % cols;
            return new int[]{rowIndex, colIndex};
        }
    }



    //region Arthur Grover Contributions

    public static class Color32 {
        private TextColor consoleColor = TextColor.DEFAULT;
        private int red = -1;
        private int green = -1;
        private int blue = -1;

        /**
         * Set the red, green, and blue values of the color you want
         * @param red Amount of red in the color
         * @param green Amount of green in the color
         * @param blue Amount of blue in the color
         * */
        public Color32(int red, int green, int blue) {
            if (!FULL_ANSI_COLOR_SUPPORT)
                throw new RuntimeException("Full ANSI Colors are not turned on/supported." +
                        "Either mark FULL_ANSI_COLOR_SUPPORT as true or supply the Console.TextColor instead");

            setRed(red);
            setGreen(green);
            setBlue(blue);
        }

        /**
         * Use the legacy Console.TextColor enum
         * @param consoleColor Predefined enum values for color
         * @see TextColor
         * */
        public Color32(TextColor consoleColor) {
            setConsoleColor(consoleColor);
        }

        /**
         * Supply a hexadecimal color code in the form 0xFFFFFF
         * @param hexcode This will be parsed in the format RRGGBB
         * */
        public Color32(int hexcode) {
            hexcode = Math.min(Math.max(hexcode, 0), 0xFFFFFF);

            setRed(hexcode >> 16 & 0xFF);
            setGreen(hexcode >> 8 & 0xFF);
            setBlue(hexcode & 0xFF);
        }

        /**
         * Pass in an existing color
         * @param color Reuse a color object
         * */
        public Color32(Color32 color) {
            setConsoleColor(color.consoleColor);
            setRed(color.getRed());
            setGreen(color.getGreen());
            setBlue(color.getBlue());
        }

        /**
         * Set the RGB values through the hue, saturation, and lightness values.
         * @param hue Hue of the color (0 - 360)
         * @param saturation Saturation of the color (0 - 1)
         * @param lightness Lightness or brightness of the color (0 - 1)
         * */
        public void setHSLValues(float hue, float saturation, float lightness) {
            hue %= 360;
            saturation = Math.max(Math.min(saturation, 1), 0);
            lightness = Math.max(Math.min(lightness, 1), 0);

            // Formula from RapidTables
            float C = (1 - Math.abs(2 * lightness - 1)) * saturation;
            float X = C * (1 - Math.abs((hue / 60) % 2 - 1));
            float m = lightness - C / 2;

            // Derived values
            float d_r = 0;
            float d_g = 0;
            float d_b = 0;

            int hueSection = (int)(hue / 60);

            switch (hueSection) {
                case 0 -> {
                    d_r = C; d_g = X; d_b = 0;
                }
                case 1 -> {
                    d_r = X; d_g = C; d_b = 0;
                }
                case 2 -> {
                    d_r = 0; d_g = C; d_b = X;
                }
                case 3 -> {
                    d_r = 0; d_g = X; d_b = C;
                }
                case 4 -> {
                    d_r = X; d_g = 0; d_b = C;
                }
                case 5 -> {
                    d_r = C; d_g = 0; d_b = X;
                }
            }

            setRed((int)((d_r + m) * 255));
            setGreen((int)((d_g + m) * 255));
            setBlue((int)((d_b + m) * 255));
        }

        /**
         * Get the hexadecimal color code of the current color
         * */
        public int getHexColor() {
            return getRed() << 16 | getGreen() << 8 | getBlue();
        }

        /**
         * Get the Hexadecimal color code from the color
         * @param color Color to convert to hexadecimal
         * */
        public static int getHexColor(Color32 color) {
            return getHexColor(color.getRed(), color.getGreen(), color.getBlue());
        }

        /**
         * Get the Hexadecimal color code from the values
         * @param red Red value
         * @param green Green value
         * @param blue Blue value
         * */
        public static int getHexColor(int red, int green, int blue) {
            return red << 16 | green << 8 | blue;
        }

        /**
         * Get the hue of the provided color
         * @param color current color
         * */
        public static float getHue(Color32 color) {
            float d_r = (float)color.getRed() / 255;
            float d_g = (float)color.getGreen() / 255;
            float d_b = (float)color.getBlue() / 255;

            int CmaxIndex = 0;
            if (d_g > d_r && d_g > d_b) CmaxIndex = 1;
            else if (d_b > d_r) CmaxIndex = 2;

            float Cmax = Math.max(Math.max(d_r, d_g), d_b);
            float Cmin = Math.min(Math.min(d_r, d_g), d_b);
            float delta = Cmax - Cmin;

            if (delta == 0) {
                return 0;
            }

            return switch (CmaxIndex) {
                case 0 -> 60 * (((d_g - d_b) / delta) % 6);
                case 1 -> 60 * (((d_b - d_r) / delta) + 2);
                case 2 -> 60 * (((d_r - d_g) / delta) + 4);
                default -> 0;
            };
        }

        /**
         * Get the saturation of the provided color
         * @param color current color
         * */
        public static float getSaturation(Color32 color) {
            float d_r = (float)color.getRed() / 255;
            float d_g = (float)color.getGreen() / 255;
            float d_b = (float)color.getBlue() / 255;

            float Cmax = Math.max(Math.max(d_r, d_g), d_b);
            float Cmin = Math.min(Math.min(d_r, d_g), d_b);
            float delta = Cmax - Cmin;

            return delta / (1 - Math.abs(2 * getLightness(color) - 1));
        }

        /**
         * Get the lightness of the provided color
         * @param color current color
         * */
        public static float getLightness(Color32 color) {
            float d_r = (float)color.getRed() / 255;
            float d_g = (float)color.getGreen() / 255;
            float d_b = (float)color.getBlue() / 255;

            float Cmax = Math.max(Math.max(d_r, d_g), d_b);
            float Cmin = Math.min(Math.min(d_r, d_g), d_b);

            return (Cmax + Cmin) / 2;
        }

        /**
         * Red value of the color
         * */
        public int getRed() {
            return red;
        }

        /**
         * Set the red value
         * @param red Set red value anywhere from 0 to 255
         * */
        public void setRed(int red) {
            this.red = Math.max(Math.min(red, 255), 0);
        }

        /**
         * Green value of the color
         * */
        public int getGreen() {
            return green;
        }

        /**
         * Set the green value
         * @param green Set green value anywhere from 0 to 255
         * */
        public void setGreen(int green) {
            this.green = Math.max(Math.min(green, 255), 0);
        }

        /**
         * Blue value of the color
         * */
        public int getBlue() {
            return blue;
        }

        /**
         * Set the blue value
         * @param blue Set blue value anywhere from 0 to 255
         * */
        public void setBlue(int blue) {
            this.blue = Math.max(Math.min(blue, 255), 0);
        }

        /**
         * Current TextColor that the console is using
         * <b>note</b> This may not get the color you're looking for.
         * Try using a different getter, like getHexColor() if necessary.
         * */
        public TextColor getConsoleColor() {
            return consoleColor;
        }

        /**
         * Set the color to a predefined color value
         * @param consoleColor Color enum for existing colors
         * @see TextColor
         * */
        public void setConsoleColor(TextColor consoleColor) {
            this.consoleColor = consoleColor;

            int color32 = switch (consoleColor) {
                case BLACK -> 0x0C0C0C;
                case RED -> 0xC50F1F;
                case GREEN -> 0x13A10E;
                case YELLOW -> 0xC19C00;
                case BLUE -> 0x0037DA;
                case PURPLE -> 0x881798;
                case CYAN -> 0x33BBC8;
                default -> 0xCCCCCC;
            };

            setRed((color32 >> 16) & 0xFF);
            setGreen((color32 >> 8) & 0xFF);
            setBlue(color32 & 0xFF);
        }
    }

    /**
     * Writes the message to the console using the provided text and RGB color
     * Does not append the newline. Text stays on the same line.
     * @param message The message to write to the console
     * @param red Amount of red value in the color
     * @param green Amount of green value in the color
     * @param blue Amount of blue value in the color
     * */
    public static void write(String message, int red, int green, int blue) {
        System.out.print(getANSIColorString(red, green, blue, false) + message + RESET);
    }

    /**
     * Writes the message to the console using the provided text and RGB color
     * Does not append the newline. Text stays on the same line.
     * @param message The message to write to the console
     * @param red Amount of red value in the color
     * @param green Amount of green value in the color
     * @param blue Amount of blue value in the color
     * @param redBack Amount of red value in the background color
     * @param greenBack Amount of green value in the background color
     * @param blueBack Amount of blue value in the background color
     * */
    public static void write(String message, int red, int green, int blue, int redBack, int greenBack, int blueBack) {
        System.out.print(
                getANSIColorString(red, green, blue, false) +
                        getANSIColorString(redBack, greenBack, blueBack, true) +
                        message +
                        RESET
        );
    }

    /**
     * Writes the message to the console using the provided text and RGB color
     * Does not append the newline. Text stays on the same line.
     * @param message The message to write to the console
     * @param textColor The color for the text
     * */
    public static void write(String message, Color32 textColor) {
        System.out.print(getANSIColorString(textColor, false) + message + RESET);
    }

    /**
     * Writes the message to the console using the provided text and RGB color
     * Does not append the newline. Text stays on the same line.
     * @param message The message to write to the console
     * @param textColor The color for the text
     * @param backgroundColor The color for the background
     * */
    public static void write(String message, Color32 textColor, Color32 backgroundColor) {
        System.out.print(
                getANSIColorString(textColor, false) +
                        getANSIColorString(backgroundColor, true) +
                        message +
                        RESET
        );
    }

    /**
     * Writes the message to the console using the provided text and RGB color
     * Append the newline to the end.
     * @param message The message to write to the console
     * @param red Amount of red value in the color
     * @param green Amount of green value in the color
     * @param blue Amount of blue value in the color
     * @param redBack Amount of red value in the background color
     * @param greenBack Amount of green value in the background color
     * @param blueBack Amount of blue value in the background color
     * */
    public static void writeln(String message, int red, int green, int blue, int redBack, int greenBack, int blueBack) {
        System.out.println(
                getANSIColorString(red, green, blue, false) +
                        getANSIColorString(redBack, greenBack, blueBack, true) +
                        message +
                        RESET
        );
    }

    /**
     * Writes the message to the console using the provided text and RGB color
     * Append the newline to the end.
     * @param message The message to write to the console
     * @param textColor The color for the text
     * */
    public static void writeln(String message, Color32 textColor) {
        System.out.println(getANSIColorString(textColor, false) + message + RESET);
    }

    /**
     * Writes the message to the console using the provided text and RGB color
     * Append the newline to the end.
     * @param message The message to write to the console
     * @param textColor The color for the text
     * @param backgroundColor The color for the background
     * */
    public static void writeln(String message, Color32 textColor, Color32 backgroundColor) {
        System.out.println(
                getANSIColorString(textColor, false) +
                        getANSIColorString(backgroundColor, true) +
                        message +
                        RESET
        );
    }

    /**
     * Writes the string to the console using System.out.print,
     * but it's more exciting 🌈
     * @param message The message to write
     */
    public static void writeRainbow(String message){
        writeRainbow(message, 0, (1.0f / message.length()) * 360);
    }

    /**
     * Writes the string to the console using System.out.print,
     * but it's more exciting 🌈
     * @param message The message to write
     * @param rotateHueAmount Amount to rotate the hue by each character
     * @param startingHue Where the hue should start
     */
    public static void writeRainbow(String message, float startingHue, float rotateHueAmount){
        Color32 color = new Color32(0);

        float rotation = startingHue;
        for (int c = 0; c < message.length(); c++) {
            color.setHSLValues(rotation, 1, 0.5f);
            write(String.valueOf(message.charAt(c)), color);
            rotation += rotateHueAmount;
        }
    }

    private static String getANSIColorString(int red, int green, int blue, boolean isBackground) {
        //noinspection StringBufferReplaceableByString
        StringBuilder sb = new StringBuilder(20);
        sb.append(isBackground ? "\u001B[48;2;" : "\u001B[38;2;");
        sb.append(red); sb.append(';');
        sb.append(green); sb.append(';');
        sb.append(blue); sb.append('m');
        return sb.toString();
    }

    private static String getANSIColorString(Color32 color, boolean isBackground) {
        //noinspection StringBufferReplaceableByString
        StringBuilder sb = new StringBuilder(20);
        sb.append(isBackground ? "\u001B[48;2;" : "\u001B[38;2;");
        sb.append(color.getRed()); sb.append(';');
        sb.append(color.getGreen()); sb.append(';');
        sb.append(color.getBlue()); sb.append('m');
        return sb.toString();
    }

    //endregion
}