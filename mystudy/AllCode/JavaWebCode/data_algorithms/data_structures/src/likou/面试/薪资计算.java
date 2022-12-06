package likou.面试;

import java.util.Scanner;

public class 薪资计算 {
    public static void main(String[] args) {
        for (int i = 100000; i <= 400000; i++) {
            if (i % 10000 == 0) {
                //i:年包
                //month:多少薪资
                int month = 13;
                //公积金
                double accumulationFund = 0.12;
                //每月房租，燃气，电费
                int rent = 3100;
                //每月自己生活费
                int livingExpenses = 2500;
                computedWages(i, month, accumulationFund, livingExpenses, rent);
            }
        }
    }

    private static double computedWages(int allSalary, int month, double accumulationFund, int livingExpenses, int rent) {
        int monthSalary = allSalary / month;
        double last = allSalary - insuranceAndFoud(accumulationFund, monthSalary)  ;//- LifeAndRent(livingExpenses, rent)- back(allSalary)
        System.out.println(allSalary + ":" + (int) last);
        return last;
    }

    private static double back(int allSalary) {
        int backSalary = allSalary - 180000;
        double sumBack = 48000 + (backSalary > 0 ? backSalary * 0.6 : 0);
        return sumBack<150000?sumBack:150000;
    }

    private static double insuranceAndFoud(double accumulationFund, int monthSalary) {
        double unemploymentInsurance = 0.02;
        double endowmentInsurance = 0.08;
        double medicalInsurance = 0.002;
        double sumInsurance = (accumulationFund + unemploymentInsurance + endowmentInsurance + medicalInsurance) * monthSalary * 12;
        return sumInsurance;
    }


    private static double LifeAndRent(int livingExpenses, int rent) {
        return (livingExpenses + rent) * 12;
    }

//    private static double tax(int allSalary) {
//
//
//        return (livingExpenses + rent) * 12;
//    }
}
