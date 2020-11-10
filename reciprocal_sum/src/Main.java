import java.util.Arrays;
import java.util.Scanner;

public class Main {

    /*
        NOTES
        amount = 100.000.000 -> Tasks are faster!
    */

    private static double[] input;

    public static void main(String[] args) {

        menu();

        long startSeq = System.currentTimeMillis();
        double sumSeq = ReciprocalArraySum.seqArraySum(input);
        long endSeq = System.currentTimeMillis();

        long startTask = System.currentTimeMillis();
        double sumTask = ReciprocalArraySum.parManyTaskArraySum(input,100);
        long endTask = System.currentTimeMillis();

        System.out.println("\n SOLUTION" +
                "\nSequential: "+sumSeq +
                "\nTasks: "+sumTask);

        System.out.println("\n BENCHMARK" +
                "\nSequential: "+(endSeq-startSeq)+"ms" +
                "\nTasks: "+(endTask-startTask)+"ms");
    }

    private static void menu(){
        try{
            System.out.print("min: ");
            double min = getDoubleInput();
            System.out.print("max: ");
            double max = getDoubleInput();
            System.out.print("amount: ");
            int amount = getIntInput();

            input = getRandomDoubleArray(min,max,amount);
        }catch(NumberFormatException ex){
            System.out.println("\nERROR");
            menu();
        }
    }

    private static double[] getRandomDoubleArray(double min, double max, int amount){
        double[] array = new double[amount];
        return Arrays.stream(array)
                .map(number -> getRandomDouble(min,max))
                .toArray();
    }

    private static double getRandomDouble(double min, double max){
        return (int)((Math.random()*(max-min)+min+1)*100)/100.0;
    }

    private static final Scanner scanner = new Scanner(System.in,"Windows-1252");

    private static double getDoubleInput() throws NumberFormatException {
        return Double.parseDouble(scanner.nextLine());
    }

    private static int getIntInput() throws NumberFormatException {
        return Integer.parseInt(scanner.nextLine());
    }
}
