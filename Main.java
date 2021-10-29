import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static ArrayList<String> parsing(String expression, String operations){
        boolean numberOnRecord = false;
        ArrayList<String> elements = new ArrayList<>();
        String parse = "";
        for (int i =0; i<expression.length(); i++){
            parse = String.valueOf(expression.charAt(i));
            if (operations.contains(parse)){
                elements.add(parse);
                parse = "";
                numberOnRecord = false;
            } else {
                if (numberOnRecord){
                    elements.set(elements.size()-1,elements.get(elements.size()-1)+parse);
                } else {
                    elements.add(parse);
                    numberOnRecord = true;
                }
            }
        }
//        System.out.println(elements);
        return elements;
    }

    public static ArrayList<String> rpn(ArrayList<String> expression, String operations){ //Reverse Polish notation
        ArrayList<String> stack = new ArrayList<>();
        ArrayList<String> result = new ArrayList<>();
        for (int i=0; i<expression.size();i++){
            String current = expression.get(i);
            if(!operations.contains(current)){
                result.add(current);
            } else{
                if(current.equals("(")){
                    stack.add(current);
                    continue;
                }
                if(current.equals(")")){
                    String transfer = stack.get(stack.size()-1);
                    stack.remove(stack.size()-1);
                    while (!transfer.equals("(")){
                        result.add(transfer);
                        transfer = stack.get(stack.size()-1);
                        stack.remove(stack.size()-1);
                        continue;
                    }
                    continue;
                }
                if(stack.size()==0) {
                    stack.add(current);
                    continue;
                }
                while (!(priorityCheck(current,stack,operations)||stack.size()==0)){
                    if(stack.get(stack.size()-1).equals("(")){
                        break;
                    }
                    result.add(stack.get(stack.size()-1));
                    stack.remove(stack.size()-1);
                }
                stack.add(current);
            }
        }
        while (stack.size()>0){
            result.add(stack.get(stack.size()-1));
            stack.remove(stack.size()-1);
        }
//        System.out.println(result);
        return result;
    }

    private static boolean priorityCheck(String current, ArrayList<String> stack, String operations) {
        for (int i=0; i<stack.size();i++){
            if(operations.indexOf(current)>operations.indexOf(stack.get(i))){
                return false;
            }
        }
        return true;
    }

    public static void calculate(ArrayList<String> parsed, String operations){
        ArrayList<ArrayList<Integer>> calculations = new ArrayList<>();
        ArrayList<ArrayList<Float>> probabilities = new ArrayList<>();
        ArrayList <ArrayList<String>> stack =  new ArrayList<>();
        for (int i=0;i<parsed.size();i++){
            if(parsed.get(i).contains("d")){
                ArrayList<Integer> calcs = new ArrayList<>();
                ArrayList<Float> probs = new ArrayList<>();
                float number = Integer.parseInt(parsed.get(i).substring(1));
                for (int j=0; j<number ; j++){
                    calcs.add(j+1);
                    probs.add(1/number);
                }
                calculations.add(calcs);
                probabilities.add(probs);
                continue;
            }
            if(operations.contains(parsed.get(i))){
                switch (parsed.get(i)){
                    case "+":
                        ArrayList<Integer> calcs = new ArrayList<>();
                        ArrayList<Float> probs = new ArrayList<>();
                        ArrayList<Integer> firstArr = calculations.get(calculations.size()-2);
                        ArrayList<Integer> secondArr = calculations.get(calculations.size()-1);

                        for (int f=0; f<firstArr.size();f++){
                            for (int s=0; s<secondArr.size();s++){
                                Integer rez =  (firstArr.get(f))+(secondArr.get(s));
                                if(calcs.contains(rez)){
                                    if((probabilities.get(probabilities.size()-2).get(f)<1)&&(probabilities.get(probabilities.size()-1).get(s)<1)){ //both probs less then 1
                                        probs.set(calcs.indexOf(rez),probs.get(calcs.indexOf(rez))+ //складываем вероятности одинаковых результатов
                                                (probabilities.get(probabilities.size()-2).get(f)*probabilities.get(probabilities.size()-1).get(s)));
                                    }else {
                                        if (probabilities.get(probabilities.size()-2).get(f)<1){ // only first prob less then 1
                                            probs.set(calcs.indexOf(rez),probs.get(calcs.indexOf(rez))+probabilities.get(probabilities.size()-2).get(f));
                                        }else
                                            probs.set(calcs.indexOf(rez),probs.get(calcs.indexOf(rez))+probabilities.get(probabilities.size()-1).get(s));// only second prob less then 1
                                    }

                                }else {
                                    calcs.add(rez);
                                    if((probabilities.get(probabilities.size()-2).get(f)<1)&&(probabilities.get(probabilities.size()-1).get(s)<1)){ //both probs less then 1
                                        probs.add(probabilities.get(probabilities.size()-2).get(f)*probabilities.get(probabilities.size()-1).get(s));
                                    }else {
                                        if (probabilities.get(probabilities.size()-2).get(f)<1){ // only first prob less then 1
                                            probs.add(probabilities.get(probabilities.size()-2).get(f));
                                        }else
                                            probs.add(probabilities.get(probabilities.size()-1).get(s));// only second prob less then 1
                                    }
                                }
                            }
                        }
                        calculations.remove(calculations.size()-2);
                        calculations.remove(calculations.size()-1);
                        probabilities.remove(probabilities.size()-2);
                        probabilities.remove(probabilities.size()-1);
                        calculations.add(calcs);
                        probabilities.add(probs);
                        break;
                    case "-":
                        ArrayList<Integer> calcsMi = new ArrayList<>();
                        ArrayList<Float> probsMi = new ArrayList<>();
                        ArrayList<Integer> firstArrMi = calculations.get(calculations.size()-2);
                        ArrayList<Integer> secondArrMi = calculations.get(calculations.size()-1);

                        for (int f=0; f<firstArrMi.size();f++){
                            for (int s=0; s<secondArrMi.size();s++){
                                Integer rez =  (firstArrMi.get(f))-(secondArrMi.get(s));
                                if(calcsMi.contains(rez)){
                                    if((probabilities.get(probabilities.size()-2).get(f)<1)&&(probabilities.get(probabilities.size()-1).get(s)<1)){ //both probs less then 1
                                        probsMi.set(calcsMi.indexOf(rez),probsMi.get(calcsMi.indexOf(rez))+ //складываем вероятности одинаковых результатов
                                                (probabilities.get(probabilities.size()-2).get(f)*probabilities.get(probabilities.size()-1).get(s)));
                                    }else {
                                        if (probabilities.get(probabilities.size()-2).get(f)<1){ // only first prob less then 1
                                            probsMi.set(calcsMi.indexOf(rez),probsMi.get(calcsMi.indexOf(rez))+probabilities.get(probabilities.size()-2).get(f));
                                        }else
                                            probsMi.set(calcsMi.indexOf(rez),probsMi.get(calcsMi.indexOf(rez))+probabilities.get(probabilities.size()-1).get(s));// only second prob less then 1
                                    }

                                }else {
                                    calcsMi.add(rez);
                                    if((probabilities.get(probabilities.size()-2).get(f)<1)&&(probabilities.get(probabilities.size()-1).get(s)<1)){ //both probs less then 1
                                        probsMi.add(probabilities.get(probabilities.size()-2).get(f)*probabilities.get(probabilities.size()-1).get(s));
                                    }else {
                                        if (probabilities.get(probabilities.size()-2).get(f)<1){ // only first prob less then 1
                                            probsMi.add(probabilities.get(probabilities.size()-2).get(f));
                                        }else
                                            probsMi.add(probabilities.get(probabilities.size()-1).get(s));// only second prob less then 1
                                    }
                                }
                            }
                        }
                        calculations.remove(calculations.size()-2);
                        calculations.remove(calculations.size()-1);
                        probabilities.remove(probabilities.size()-2);
                        probabilities.remove(probabilities.size()-1);
                        calculations.add(calcsMi);
                        probabilities.add(probsMi);
                        break;

                    case ">":
                        ArrayList<Integer> calcsMr = new ArrayList<>();
                        ArrayList<Float> probsMr = new ArrayList<>();
                        probsMr.add((float) 0);
                        probsMr.add((float) 0);
                        ArrayList<Integer> firstArrMr = calculations.get(calculations.size()-2);
                        ArrayList<Integer> secondArrMr = calculations.get(calculations.size()-1);
                        ArrayList<Float> firstProbs = probabilities.get(calculations.size()-2);
                        ArrayList<Float> secondProbs = probabilities.get(calculations.size()-1);
                        int yes=0;
                        for (int f=0; f<firstArrMr.size();f++){
                            for (int s=0; s<secondArrMr.size();s++){
                                if (firstArrMr.get(f)>secondArrMr.get(s)) {
                                    probsMr.set(1,probsMr.get(1)+(firstProbs.get(f)*secondProbs.get(s)));
                                    yes++;
                                }else {
                                    probsMr.set(0,probsMr.get(0)+(firstProbs.get(f)*secondProbs.get(s)));
                                }
                            }
                        }
                        calcsMr.add(0);
                        calcsMr.add(1);
//                        probsMr.set(0, probsMr.get(0)/firstArrMr.size()*secondArrMr.size());
//                        probsMr.set(1, probsMr.get(1)/firstArrMr.size()*secondArrMr.size());
                        calculations.remove(calculations.size()-2);
                        calculations.remove(calculations.size()-1);
                        probabilities.remove(probabilities.size()-2);
                        probabilities.remove(probabilities.size()-1);
                        calculations.add(calcsMr);
                        probabilities.add(probsMr);
                        break;
                    case "*":
                        ArrayList<Integer> calcsMu = new ArrayList<>();
                        ArrayList<Float> probsMu = new ArrayList<>();
                        ArrayList<Integer> firstArrMu = calculations.get(calculations.size()-2);
                        ArrayList<Integer> secondArrMu = calculations.get(calculations.size()-1);

                        for (int f=0; f<firstArrMu.size();f++){
                            for (int s=0; s<secondArrMu.size();s++){
                                Integer rez =  (firstArrMu.get(f))*(secondArrMu.get(s));
                                if(calcsMu.contains(rez)){
                                    if((probabilities.get(probabilities.size()-2).get(f)<1)&&(probabilities.get(probabilities.size()-1).get(s)<1)){ //both probs less then 1
                                        probsMu.set(calcsMu.indexOf(rez),probsMu.get(calcsMu.indexOf(rez))+ //складываем вероятности одинаковых результатов
                                                (probabilities.get(probabilities.size()-2).get(f)*probabilities.get(probabilities.size()-1).get(s)));
                                    }else {
                                        if (probabilities.get(probabilities.size()-2).get(f)<1){ // only first prob less then 1
                                            probsMu.set(calcsMu.indexOf(rez),probsMu.get(calcsMu.indexOf(rez))+probabilities.get(probabilities.size()-2).get(f));
                                        }else
                                            probsMu.set(calcsMu.indexOf(rez),probsMu.get(calcsMu.indexOf(rez))+probabilities.get(probabilities.size()-1).get(s));// only second prob less then 1
                                    }

                                }else {
                                    calcsMu.add(rez);
                                    if((probabilities.get(probabilities.size()-2).get(f)<1)&&(probabilities.get(probabilities.size()-1).get(s)<1)){ //both probs less then 1
                                        probsMu.add(probabilities.get(probabilities.size()-2).get(f)*probabilities.get(probabilities.size()-1).get(s));
                                    }else {
                                        if (probabilities.get(probabilities.size()-2).get(f)<1){ // only first prob less then 1
                                            probsMu.add(probabilities.get(probabilities.size()-2).get(f));
                                        }else
                                            probsMu.add(probabilities.get(probabilities.size()-1).get(s));// only second prob less then 1
                                    }
                                }
                            }
                        }
                        calculations.remove(calculations.size()-2);
                        calculations.remove(calculations.size()-1);
                        probabilities.remove(probabilities.size()-2);
                        probabilities.remove(probabilities.size()-1);
                        calculations.add(calcsMu);
                        probabilities.add(probsMu);
                        break;
                }
                continue;
            }
            ArrayList<Integer> calcs = new ArrayList<>();
            ArrayList<Float> probs = new ArrayList<>();
            calcs.add(Integer.valueOf(parsed.get(i)));
            probs.add((float) 1);
            //just number
            calculations.add(calcs);
            probabilities.add(probs);
        }
//        System.out.println(calculations);
//        System.out.println(probabilities);

        sortAndPrint(calculations.get(0),probabilities.get(0));
    }

    private static void sortAndPrint(ArrayList<Integer> calculations, ArrayList<Float> probabilities) {

        ArrayList<Integer> sorted = new ArrayList<>();
        for (int c:calculations
             ) {
            sorted.add(c);
        }
        sorted.sort(Integer::compareTo);
//        System.out.println("s:"+sorted);
//        System.out.println("c:"+calculations);

//        for (int i=0; i<calculations.size();i++){
//            String result = String.format("%.2f",probabilities.get(i)*100);
//            System.out.println(calculations.get(i)+" "+result);
//        }
        for (int i=0; i<calculations.size();i++){

            String result = String.format("%.2f",probabilities.get(calculations.indexOf(sorted.get(i)))*100);
            System.out.println(calculations.get(calculations.indexOf(sorted.get(i)))+" "+result);
//            System.out.println("s:"+sorted.get(i)+" cal.ind:"+calculations.get(calculations.indexOf(sorted.get(i)))+" p:"+probabilities.get(i)+"p.ind:"+probabilities.get(calculations.indexOf(sorted.get(i))));
        }

//        while (calculations.size()>0){
//
//        }
    }


    public static void main(String[] args) {
        String operations = "*+->()";
        String test = "d4+(d6>2)";
//        String test = "(6+10-4)*(1+1*2)+1";







//        calculate(rpn(parsing(test,operations),operations),operations);
        Scanner in = new Scanner(System.in);
        test = in.nextLine();
//        System.out.println(parsing(test,operations));
//        System.out.println(rpn(parsing(test,operations),operations));
//        (8+2*5)*(1+3*2-4)
        calculate(rpn(parsing(test,operations),operations),operations);
//        d3*(d4+d5)
//        (d3>d4)+d5
    }
}