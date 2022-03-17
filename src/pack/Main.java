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

    public static int DECIMALS = 128;

    public static void main(String[] args) throws FileNotFoundException {

        File file = new File("D:\\ULE\\3º\\SI\\datos_4.txt");

        /** FUENTE DE INFORMACIÓN */
        ArrayList<Alfabeto> lista = generarLista(file);
        System.out.println("Entropía en bits: "+calcularEntropia(lista));

        /** NUMERO A MENSAJE */
        BigDecimal num = new BigDecimal("0.938434993679600223922608511494132796026424143844362665527995868729657277");
        int longuitud=57;
        dividirFuente(lista);

        String msg = calcularMensaje(longuitud, num, lista);
        System.out.println("El mensaje codificado es: "+msg);

        /** INFO DE LA FUENTE EXTRA */
        /*for(int i=0; i<lista.size(); i++){
            System.out.print(lista.get(i).getChar() + " - ");
            System.out.println(lista.get(i).getL() + " , " + lista.get(i).getH());
        }*/

        /*for(int i=0; i<lista.size(); i++){
            System.out.print(i+1 + " - ");
            System.out.println(lista.get(i).imprimir());
        }*/
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

    private static int checkExist(ArrayList<Alfabeto> list, char c){
        for(int i=0; i<list.size(); i++){
            if(list.get(i).getChar()==c){ return i; }
        }

        return -1;
    }

    private static double calcularEntropia(ArrayList<Alfabeto> list){
        double ent = 0.0;
        for(int i=0; i<list.size(); i++){
            double prob = list.get(i).getProbabilidad();
            double log = (Math.log(1/prob) / Math.log(2));
            ent+= (prob * log);
        }
        return redondearDecimal(ent,5);
    }

    private static String calcularMensaje(int lon, BigDecimal num, ArrayList<Alfabeto> list){
        String[] msg = new String[lon];
        BigDecimal numActual = num;
        for(int i=0; i<lon; i++){
            BigDecimal Lj = new BigDecimal(0);
            BigDecimal Hj = new BigDecimal(0);
            for(int x=0; x<list.size(); x++){
                int bool = numActual.compareTo(list.get(x).getL());
                int bool2 = numActual.compareTo(list.get(x).getH());
                if(bool>=0 && bool2==-1){
                    Lj = list.get(x).getL();
                    Hj = list.get(x).getH();
                    msg[i] = String.valueOf(list.get(x).getChar());
                    break;
                }
            }
            numActual = (numActual.subtract(Lj)).divide(Hj.subtract(Lj), DECIMALS, RoundingMode.HALF_UP);
        }

        /** STRING[] A STRING */
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < msg.length; i++) {
            if(msg[i].equals("⎵")) msg[i]=" ";
            sb.append(msg[i]);
        }

        return sb.toString();
    }

    private static void dividirFuente(ArrayList<Alfabeto> lista){
        int total=0;
        for(int i=0; i<lista.size(); i++){
            total+=lista.get(i).getFrecuencia();
        }
        BigDecimal div = new BigDecimal(1).divide(BigDecimal.valueOf(total), DECIMALS, RoundingMode.HALF_UP);
        BigDecimal actualDiv= BigDecimal.valueOf(0);
        for(int i=0; i<lista.size(); i++){
            BigDecimal nxtDiv = actualDiv.add(div.multiply(BigDecimal.valueOf(lista.get(i).getFrecuencia())));
            if(i==lista.size()-1) nxtDiv= BigDecimal.valueOf(1);
            lista.get(i).setLH(actualDiv,nxtDiv);
            actualDiv=nxtDiv;
            /*if(lista.get(i).getChar()=='n'){
                System.out.println(lista.get(i).getChar()+"- "+ lista.get(i).getL() +" - "+ lista.get(i).getH());
            }*/
        }
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


