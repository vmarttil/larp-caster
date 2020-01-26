/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.kapsi.vmarttil.larpcaster.domain;

/**
 *
 * @author vmarttil
 */
public class Laskuri implements Comparable<Laskuri> {
    String laskettava;
    int maara;
    
    public Laskuri(String laskettava) {
        if (laskettava == null) {
            this.laskettava = "-";
        } else {
            this.laskettava = laskettava;
        }
        this.maara = 0;
    }
    
    public void kasvata() {
        this.maara = this.maara + 1;
    }
    
    public Laskuri tallenna() {
        Laskuri tallenne = new Laskuri(this.laskettava);
        tallenne.maara = this.maara;
        return tallenne;        
    }
    
    // Getterit

    public String getLaskettava() {
        return this.laskettava;
    }

    public int getMaara() {
        return this.maara;
    }
    
    // Setterit

    public void setLaskettava(String laskettava) {
        this.laskettava = laskettava;
    }

    public void setMaara(int maara) {
        this.maara = maara;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Laskuri) {
            Laskuri toinenLaskuri = (Laskuri) o;
            if (toinenLaskuri.getLaskettava().equals(this.getLaskettava()) && toinenLaskuri.getMaara() == this.getMaara()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    
    @Override
    public int compareTo(Laskuri l) {
        if (this.getLaskettava().equals("-")) {
            return 1;
        } else if (l.getLaskettava().equals("-")) {
            return -1;
        } else if (l.getMaara() - this.getMaara() != 0) {
            return l.getMaara() - this.getMaara();
        } else {
            return this.getLaskettava().compareTo(l.getLaskettava());
        }
    }
    
    
    
}
