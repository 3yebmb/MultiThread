public class Method {
    final int size = 10000000; // количество элементов в исходном массиве
    final int countThread = 11; // количество потоков для второго метода
    int h = size / countThread; // размерность подмассива
    float[][] subArr = new float[countThread][]; // массив с массивами (:
    int last = h; // рзамерноть последнего массива
    int pos = 0; // смещение
    long timeOperation; // время потраченное на операцию
    ThreadClass[] threads = new ThreadClass[countThread]; // массив с объектами потока

    final float[] arr = new float[size]; // исходный массив
    final int l = arr.length % countThread; // определение остатка от деления ( для ровного разделения исходного массива
                                            // на подмассивы

    // метод запускающий разные варианты (с одним или несколькими потоками)
    public void go(int method) throws InterruptedException {
        for (int i = 0; i < size; i++) {
            arr[i] = 1;
        }
        switch (method) {
            case 1 :
                ThreadClass th1 = new ThreadClass(0, arr);
                timeOperation = System.currentTimeMillis();
                th1.start();
                th1.join();
                timeOperation = System.currentTimeMillis() - timeOperation;
                System.out.println("На заполнение элементов массива новыми значениями 1 потоку потребовалось " + timeOperation + " мс");
                break;
            case 2 :
                timeOperation = System.currentTimeMillis();
                prepareArray();
                timeOperation = System.currentTimeMillis() - timeOperation;
                System.out.println("На разбиение массива на " + countThread + " частей ушло " + timeOperation + " мс");
                timeOperation = System.currentTimeMillis();
                for (int i = 0; i < countThread; i++) {
                    threads[i] = new ThreadClass(pos, subArr[i]);
                    threads[i].start();
                    threads[i].join();
                    pos += subArr.length;
                }
                timeOperation = System.currentTimeMillis() - timeOperation;
                System.out.println("На заполнение элементов массива новыми значениями " + countThread + " потокам потребовалось " + timeOperation + " мс");

                break;
        }
    }

    // метод разделения массива на равные части
    private void prepareArray(){

        if ((l !=0)){
            h += 1;
            last = (arr.length % ((countThread-1)*h));
        }

        for (int i = 0; i < countThread-1; i++) {
            subArr[i] = new float[h];
            System.arraycopy(arr, pos, subArr[i], 0, h);
            pos += h;
//            System.out.println(java.util.Arrays.deepToString(new float[][]{subArr[i]}));
        }
        subArr[countThread-1] = new float[last];
        System.arraycopy(arr, pos, subArr[countThread-1], 0, last);
        pos = 0;
//        System.out.println(java.util.Arrays.deepToString(new float[][]{subArr[countThread-1]}));
    }
}