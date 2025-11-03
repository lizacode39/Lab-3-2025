import functions.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("ТЕСТ: LinkedListTabulatedFunction\n");

        TabulatedFunction func = new LinkedListTabulatedFunction(0, 5, new double[]{1, 2, 3, 4, 5, 6});

        System.out.println("Левая граница: " + func.getLeftDomainBorder());
        System.out.println("Правая граница: " + func.getRightDomainBorder());
        System.out.println("f(2.5) = " + func.getFunctionValue(2.5));

        try {
            func.getPoint(-1);
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("FunctionPointIndexOutOfBoundsException: " + e.getMessage());
        }

        try {
            func.setPointX(0, 10.0);
        } catch (InappropriateFunctionPointException e) {
            System.out.println("InappropriateFunctionPointException: " + e.getMessage());
        }

        try {
            TabulatedFunction small = new LinkedListTabulatedFunction(0, 2, new double[]{1, 2, 3});
            small.deletePoint(0);
            small.deletePoint(0);
            small.deletePoint(0);
        } catch (IllegalStateException e) {
            System.out.println("IllegalStateException: " + e.getMessage());
        }

        System.out.println("\nКОРРЕКТНОСТЬ РАБОТЫ");
        System.out.println("Добавляем точку (2.5, 100.0)");
        try {
            func.addPoint(new FunctionPoint(2.5, 100.0));
            System.out.println("Точка добавлена успешно");
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }

        System.out.println("\nТочки после добавления:");
        ((LinkedListTabulatedFunction) func).printList();

        System.out.println("\nf(2.5) = " + func.getFunctionValue(2.5));
    }
}