package pack;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Alberto Gutiérrez Morán
 */

public class Main {

    public static int DECIMALS = 600;

    public static void main(String[] args) throws FileNotFoundException {

        File file = new File("D:\\Alberto GM\\ULE\\3º\\SI\\datos_3.txt");

        /** FUENTE DE INFORMACIÓN */
        ArrayList<Alfabeto> lista = generarLista(file);
        dividirFuente(lista);
        ArrayList<Character> listaChar = generarListaChar(file);

        /** CODIFICAR */
        BigDecimal out = descodificar(lista, listaChar);
        System.out.println(out);
    }

    private static BigDecimal descodificar(ArrayList<Alfabeto> listaA, ArrayList<Character> listaC){
        int pos = checkExist(listaA,listaC.get(0));
        BigDecimal L = listaA.get(pos).getL();
        BigDecimal H = listaA.get(pos).getH();

        for(int i=1; i<listaC.size(); i++){
            pos = checkExist(listaA,listaC.get(i));
            BigDecimal Ln = listaA.get(pos).getL();
            BigDecimal Hn = listaA.get(pos).getH();
            BigDecimal Lj = L;
            L = L.add((H.subtract(L)).multiply(Ln));
            H = Lj.add((H.subtract(Lj)).multiply(Hn));
        }
        L = L.setScale(DECIMALS, RoundingMode.HALF_UP);
        H = L.setScale(DECIMALS, RoundingMode.HALF_UP);
        BigDecimal out = obtenerDescodificacion(L,H);
        return out;
    }

    private static BigDecimal obtenerDescodificacion(BigDecimal L, BigDecimal H){
        int lSize = Math.max(0, L.stripTrailingZeros().scale());
        int hSize = Math.max(0, H.stripTrailingZeros().scale());
        int dec=-1, i=1;

        for(i=1; i<=lSize; i++){
            int x = i*-1;
            if(i<=hSize){
                dec = getDigit(L,x);
                dec++;
                if(dec<getDigit(H,x)){
                    break;
                }
            }else{
                dec = getDigit(L,x);
                dec++;
                if(dec<10) break;
            }
        }

        BigDecimal out;
        if(i<=lSize){
            out = L.setScale((i-1), RoundingMode.FLOOR);
            BigDecimal lastDigit = (BigDecimal.valueOf(10).pow(i*-1)).multiply(BigDecimal.valueOf(getDigit(L,dec)));
            out=out.add(lastDigit);
        }else{
            out = L;
        }

        return out;
    }

    private static ArrayList<Alfabeto> generarLista(File file) throws FileNotFoundException {
        Scanner sc = new Scanner(file);
        ArrayList<Alfabeto> lista = new ArrayList<Alfabeto>();
        int total=0;

        while (true) {
            String nxt = sc.nextLine();
            for(int i=0; i<nxt.length(); i++){
                char c = nxt.charAt(i);
                if(c==' '){c='⎵';}
                int x = checkExist(lista, c);
                if(x==-1){
                    Alfabeto newletter = new Alfabeto(c);
                    lista.add(newletter);
                }else{
                    lista.get(x).aumentarFrecuencia();
                }
                total++;
            }

            if(sc.hasNextLine()){
                cambioDeLinea(lista);
                total+=2;
            }else{
                break;
            }
        }

        for(int i=0; i<lista.size(); i++){
            lista.get(i).setProbabilidad(total);
        }

        return lista;
    }

    private static ArrayList<Character> generarListaChar(File file) throws FileNotFoundException {
        Scanner sc = new Scanner(file);
        ArrayList<Character> lista = new ArrayList<>();

        while (true) {
            String nxt = sc.nextLine();
            for(int i=0; i<nxt.length(); i++){
                char c = nxt.charAt(i);
                lista.add(c);
            }

            if(sc.hasNextLine()){
                lista.add(' ');
            }else{
                break;
            }
        }

        return lista;
    }

    private static int checkExist(ArrayList<Alfabeto> list, char c){
        if(c==' ') c='⎵';
        for(int i=0; i<list.size(); i++){
            if(list.get(i).getChar()==c){ return i; }
        }

        return -1;
    }

    private static void dividirFuente(ArrayList<Alfabeto> lista){
        int total=0;
        for(int i=0; i<lista.size(); i++){
            total+=lista.get(i).getFrecuencia();
        }
        System.out.println(total);
        BigDecimal div = new BigDecimal(1).divide(BigDecimal.valueOf(total), DECIMALS, RoundingMode.HALF_UP);
        BigDecimal actualDiv= BigDecimal.valueOf(0);
        for(int i=0; i<lista.size(); i++){
            BigDecimal nxtDiv = actualDiv.add(div.multiply(BigDecimal.valueOf(lista.get(i).getFrecuencia())));
            if(i==lista.size()-1) nxtDiv= BigDecimal.valueOf(1);
            lista.get(i).setLH(actualDiv,nxtDiv);
            actualDiv=nxtDiv;
        }
    }

    public static int getDigit(BigDecimal bd, int position) {
        BigDecimal multiplier = BigDecimal.ONE.divide(BigDecimal.TEN);
        if (position < 0) {
            position = -position;
            multiplier = BigDecimal.TEN;
        }
        BigDecimal power = multiplier.pow(position);
        BigDecimal value = bd.abs().multiply(power).remainder(BigDecimal.TEN);

        return value.intValue();
    }

    private static void cambioDeLinea(ArrayList<Alfabeto> list){
        char c='⎵';
        int x = checkExist(list, c);
        if(x==-1){
            Alfabeto newletter = new Alfabeto(c);
            newletter.aumentarFrecuencia();
            list.add(newletter);
        }else{
            list.get(x).aumentarFrecuencia();
            list.get(x).aumentarFrecuencia();
        }
    }

    private static double redondearDecimal(double x, int dec){
        double resultado = x;
        double parteEntera = Math.floor(resultado);
        resultado=(resultado-parteEntera)*Math.pow(10, dec);
        resultado=Math.round(resultado);
        resultado=(resultado/Math.pow(10, dec))+parteEntera;
        return resultado;
    }

}


