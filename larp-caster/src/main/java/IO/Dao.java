/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IO;

import java.util.List;

/**
 *
 * @author vmarttil
 */
public interface Dao<O, T> {
    void tallenna(O objekti) throws Exception;
    O lue(T tunnus) throws Exception;
    void paivita(O objekti) throws Exception;
    void poista(T avain) throws Exception;
    List<O> listaa() throws Exception;
}
