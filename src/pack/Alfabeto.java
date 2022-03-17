package pack;

import java.math.BigDecimal;

public class Alfabeto {
    private char caracter;
    private int frecuencia;
    private double probabilidad;
    private BigDecimal L;
    private BigDecimal H;

    Alfabeto(char c){
        this.frecuencia=1;
        this.caracter = c;
    }

    ///GETTERS AND SETTERS///
    public char getChar(){ return this.caracter; }

    public void setFrecuencia(int f){ this.frecuencia = f; }
    public int getFrecuencia(){ return this.frecuencia; }
    public void aumentarFrecuencia(){ this.frecuencia++; }

    public void setProbabilidad(int total){ this.probabilidad = (double) frecuencia/total; }
    public double getProbabilidad(){ return this.probabilidad; }

    public void setLH(BigDecimal L, BigDecimal H) {this.L = L; this.H = H;}
    public BigDecimal getL(){return L;}
    public BigDecimal getH(){return H;}

    public String imprimir(){
        String data;
        data = "Símbolo: " +this.caracter +" | Frecuencia: " +this.frecuencia+" | Probabilidad: "+redondearDecimal(this.probabilidad);
        return data;
    }

    private double redondearDecimal(double x){
        double resultado = x;
        double parteEntera = Math.floor(resultado);
        resultado=(resultado-parteEntera)*Math.pow(10, 4);
        resultado=Math.round(resultado);
        resultado=(resultado/Math.pow(10, 4))+parteEntera;
        return resultado;
    }


}
