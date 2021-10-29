import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class hh1 {

//    public static void randomTests(int testsNum){
//        int a = 0;
//        int b = 1000;
//        int random_number1 = a + (int) (Math.random() * b); // Генерация 1-го числа
//        for (int i=0; i<testsNum; i++){
//            int billsNum=(int) (Math.random() * 15);
//            System.out.println("b="+billsNum);
//            int workersNum =1+(int) (Math.random() * 20);
//            System.out.println("w="+workersNum);
//            List<Integer> bills = new ArrayList<>();
//            for (int j=0; j<billsNum; j++)
//            {
//                bills.add((int) (Math.random() * b));
//                System.out.println(bills.get(bills.size()-1));
//            }
//            System.out.println("result");
//            System.out.println(binarySearch(bills,workersNum));
//        }
////        System.out.println("1-ое случайное число: " + random_number1);
//    }

    public static int findSum(List<Integer> bills){
        int sum =0;
        for (int b:bills
             ) {
            sum = sum+b;
        }
        return (sum);
    }
    public static int howManyFitIn(List<Integer> bills, int pay){
        int result = 0;
        for (int b:bills
             ) {
            int bill = b;
            while (bill>=pay){
                result++;
                bill = bill-pay;
            }
        }
        return result;
    }
    public static int binarySearch(List<Integer> bills, int workersNum){
        int minPay = 1; //head
        int maxPay = findSum(bills)/workersNum;//tale
        int pay = maxPay;

        while (minPay!=maxPay){
            if(howManyFitIn(bills, pay)>=workersNum) {
                minPay = pay;
                if (maxPay-minPay<4){
                    return finalization(bills, maxPay, minPay, workersNum);
                }else {
                    pay = maxPay - ((maxPay - minPay) / 2);
                }
            }else {
                maxPay = pay;
                pay = maxPay - ((maxPay - minPay) / 2);
                if (maxPay-minPay<4){
                    return finalization(bills, maxPay, minPay, workersNum);
                }
            }
        }
        return pay;
    }

    private static int finalization(List<Integer> bills, int maxPay, int minPay, int workersNum) {
        if (howManyFitIn(bills, minPay+3)>=workersNum)
            return minPay+3;
        if (howManyFitIn(bills, minPay+2)>=workersNum)
            return minPay+2;
        if (howManyFitIn(bills,minPay+1)>=workersNum)
            return minPay+1;
        return minPay;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String line = in.nextLine();
        String firstLine[] = new String[2];
        firstLine = line.split(" ");
        int billsNum = Integer.parseInt(firstLine[0]);
        int workersNum = Integer.parseInt(firstLine[1]);
        List<Integer> bills = new ArrayList<>();

        for (int i =0; i< billsNum; i++){
            line = in.nextLine();
            bills.add(Integer.parseInt(line));
        }

        int sum = findSum(bills);
        if(sum<workersNum){
            System.out.println(0);
            return;
        }
        if (sum==workersNum){
            System.out.println(1);
            return;
        }
        System.out.println(binarySearch(bills,workersNum));
//        randomTests(10);
    }
}
